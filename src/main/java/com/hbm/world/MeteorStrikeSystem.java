package com.hbm.world;

import com.hbm.config.NtmConfig;
import com.hbm.entity.NtmEntityTypes;
import com.hbm.entity.projectile.Meteor;
import com.hbm.handler.ArmorModHandler;
import com.hbm.items.NtmItems;
import com.hbm.util.Vec3NT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MeteorStrikeSystem {

    private static final RandomSource METEOR_RANDOM = RandomSource.create();
    private static final Map<String, Integer> METEOR_SHOWERS = new HashMap<>();

    private MeteorStrikeSystem() { }

    public static void update(MinecraftServer server) {
        if (!NtmConfig.COMMON.ENABLE_METEOR_STRIKES.get()) {
            METEOR_SHOWERS.clear();
            return;
        }

        for (ServerLevel level : server.getAllLevels()) {
            meteorUpdate(level);
        }
    }

    private static void meteorUpdate(ServerLevel level) {
        String dimensionKey = level.dimension().location().toString();
        int meteorShower = METEOR_SHOWERS.getOrDefault(dimensionKey, 0);
        int chance = meteorShower > 0 ? NtmConfig.COMMON.METEOR_SHOWER_CHACE.get() : NtmConfig.COMMON.METEOR_STRIKE_CHACE.get();

        if (chance > 0 && METEOR_RANDOM.nextInt(chance) == 0) {
            List<ServerPlayer> players = level.players();
            if (!players.isEmpty()) {
                ServerPlayer player = players.get(level.random.nextInt(players.size()));
                if (shouldSpawnMeteorFor(player)) {
                    spawnMeteorAtPlayer(player, hasProtection(player));
                }
            }
        }

        if (meteorShower > 0) {
            meteorShower--;
            METEOR_SHOWERS.put(dimensionKey, meteorShower);
        }

        if (NtmConfig.COMMON.ENABLE_METEOR_SHOWERS.get()
                && NtmConfig.COMMON.METEOR_STRIKE_CHACE.get() > 0
                && METEOR_RANDOM.nextInt(NtmConfig.COMMON.METEOR_STRIKE_CHACE.get() * 100) == 0) {
            int maxDuration = NtmConfig.COMMON.METEOR_SHOWER_DURATION.get();
            int duration = (int) (maxDuration * 0.75D + maxDuration * 0.25D * METEOR_RANDOM.nextFloat());
            METEOR_SHOWERS.put(dimensionKey, duration);
        }
    }

    private static boolean shouldSpawnMeteorFor(Player player) {
        return getHelmetMod(player) != NtmItems.METEOR_CHARM.get();
    }

    private static boolean hasProtection(Player player) {
        return getHelmetMod(player) == NtmItems.PROTECTION_CHARM.get();
    }

    private static Item getHelmetMod(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.isEmpty() || !ArmorModHandler.hasMods(helmet)) {
            return null;
        }

        ItemStack mod = ArmorModHandler.pryMods(player.level(), helmet)[ArmorModHandler.HELMET_ONLY];
        return mod.isEmpty() ? null : mod.getItem();
    }

    public static void spawnMeteorAtPlayer(Player player, boolean safe) {
        Meteor meteor = new Meteor(NtmEntityTypes.METEOR.get(), player.level());
        double spawnX = player.getX() + METEOR_RANDOM.nextInt(201) - 100;
        double spawnZ = player.getZ() + METEOR_RANDOM.nextInt(201) - 100;
        meteor.setPos(spawnX, 384.0D, spawnZ);

        Vec3NT vector;
        if (safe) {
            vector = new Vec3NT(meteor.getX() - player.getX(), 0.0D, meteor.getZ() - player.getZ()).normalizeSelf();
            double speed = METEOR_RANDOM.nextDouble();
            vector.xCoord *= speed;
            vector.zCoord *= speed;
            meteor.safe = true;
        } else {
            vector = new Vec3NT(METEOR_RANDOM.nextDouble() - 0.5D, 0.0D, 0.0D);
            vector.rotateAroundYRad(Math.PI * METEOR_RANDOM.nextDouble());
        }

        meteor.setDeltaMovement(vector.xCoord, -2.5D, vector.zCoord);
        player.level().addFreshEntity(meteor);
    }
}
