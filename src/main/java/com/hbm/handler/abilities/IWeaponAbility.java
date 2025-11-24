package com.hbm.handler.abilities;

import com.hbm.util.ContaminationUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;

public interface IWeaponAbility extends IBaseAbility {
    // Note: tool is currently unused in weapon abilities
    void onHit(int lvl, Level level, Player player, Entity victim, Item tool);

    int SORT_ORDER_BASE = 200;

    // region handlers
    IWeaponAbility NONE = new IWeaponAbility() {
        @Override
        public String getName() {
            return "";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 0;
        }

        @Override
        public void onHit(int lvl, Level level, Player player, Entity victim, Item tool) { }
    };

    IWeaponAbility RADIATION = new IWeaponAbility() {
        @Override
        public String getName() {
            return "weapon.ability.radiation";
        }

        public final float[] radAtLevel = { 15F, 50F, 500F };

        @Override
        public int levels() {
            return radAtLevel.length;
        }

        @Override
        public String getExtension(int level) {
            return " (" + radAtLevel[level] + ")";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 1;
        }

        @Override
        public void onHit(int lvl, Level level, Player player, Entity victim, Item tool) {
            if (victim instanceof LivingEntity livingVictim) {
                ContaminationUtil.contaminate(livingVictim, ContaminationUtil.HazardType.RADIATION, ContaminationUtil.ContaminationType.CREATIVE, radAtLevel[lvl]);
            }
        }
    };

    IWeaponAbility VAMPIRE = new IWeaponAbility() {
        @Override
        public String getName() {
            return "weapon.ability.vampire";
        }

        public final float[] amountAtLevel = { 2F, 3F, 5F, 10F, 50F };

        @Override
        public int levels() {
            return amountAtLevel.length;
        }

        @Override
        public String getExtension(int level) {
            return " (" + amountAtLevel[level] + ")";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 2;
        }

        @Override
        public void onHit(int lvl, Level level, Player player, Entity victim, Item tool) {
            float amount = amountAtLevel[lvl];

            if (victim instanceof LivingEntity livingVictim) {
                if (livingVictim.getHealth() <= 0) return;
                livingVictim.setHealth(livingVictim.getHealth() - amount);
                if (livingVictim.getHealth() <= 0) livingVictim.die(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)));
                player.heal(amount);
            }
        }
    };

    IWeaponAbility STUN = new IWeaponAbility() {
        @Override
        public String getName() {
            return "weapon.ability.stun";
        }

        public final int[] durationAtLevel = { 2, 3, 5, 10, 15 };

        @Override
        public int levels() {
            return durationAtLevel.length;
        }

        @Override
        public String getExtension(int level) {
            return " (" + durationAtLevel[level] + ")";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 3;
        }

        @Override
        public void onHit(int lvl, Level level, Player player, Entity victim, Item tool) {
            int duration = durationAtLevel[lvl];

            if (victim instanceof LivingEntity livingVictim) {

                livingVictim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration * 20, 4));
                livingVictim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration * 20, 4));
            }
        }
    };

    IWeaponAbility BEHEADER = new IWeaponAbility() {
        @Override
        public String getName() {
            return "weapon.ability.beheader";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 8;
        }

        @Override
        public void onHit(int level, Level world, Player player, Entity victim, Item tool) {
            if (victim instanceof LivingEntity living && living.getHealth() <= 0.0F) {
                switch (living) {
                    case Skeleton ignored -> living.spawnAtLocation(new ItemStack(Items.SKELETON_SKULL), 0.0F);
                    case WitherSkeleton ignored -> {
                        if (world.random.nextInt(20) == 0) {
                            living.spawnAtLocation(new ItemStack(Items.WITHER_SKELETON_SKULL), 0.0F);
                        } else {
                            living.spawnAtLocation(new ItemStack(Items.COAL, 3), 0.0F);
                        }
                    }
                    case Zombie ignored -> living.spawnAtLocation(new ItemStack(Items.ZOMBIE_HEAD), 0.0F);
                    case Creeper ignored -> living.spawnAtLocation(new ItemStack(Items.CREEPER_HEAD), 0.0F);
                    case MagmaCube ignored -> living.spawnAtLocation(new ItemStack(Items.MAGMA_CREAM, 3), 0.0F);
                    case Slime ignored -> living.spawnAtLocation(new ItemStack(Items.SLIME_BALL, 3), 0.0F);
                    case Player targetPlayer -> {
                        ItemStack head = new ItemStack(Items.PLAYER_HEAD);
                        head.set(DataComponents.PROFILE, new ResolvableProfile(targetPlayer.getGameProfile()));
                        living.spawnAtLocation(head, 0.0F);
                    }
                    default -> {
                        living.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH, 3), 0.0F);
                        living.spawnAtLocation(new ItemStack(Items.BONE, 2), 0.0F);
                    }
                }
            }
        }
    };

    IWeaponAbility[] abilities = { NONE, RADIATION, VAMPIRE, STUN, /*PHOSPHORUS, FIRE, CHAINSAW,*/ BEHEADER, /*BOBBLE*/ };

    static IWeaponAbility getByName(String name) {
        for(IWeaponAbility ability : abilities) {
            if(ability.getName().equals(name))
                return ability;
        }

        return NONE;
    }
}
