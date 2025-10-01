package com.hbm.items.tools;

import com.hbm.particle.ParticleDebris;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Pewpew extends Item {
    public Pewpew(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        if (level.isClientSide) {
            ParticleDebris fx = new ParticleDebris((ClientLevel) level, x, y, z);

            Vec3 motion = new Vec3(1F, 0, 0);
            motion = motion.yRot((float) (level.random.nextDouble() * Math.PI * 2));
            motion = motion.zRot((float) Math.toRadians(-(45 + level.random.nextFloat() * 25)));

            fx.setDeltaMovement(motion);

            Minecraft.getInstance().particleEngine.add(fx);;
        }

        return InteractionResultHolder.success(stack);
    }
}
