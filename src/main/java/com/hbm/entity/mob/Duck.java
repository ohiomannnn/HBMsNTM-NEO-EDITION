package com.hbm.entity.mob;

import com.hbm.lib.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;

public class Duck extends Chicken {

    public Duck(EntityType<? extends Chicken> type, Level level) {
        super(type, level);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.DUCK.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.DUCK.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.DUCK.get();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Chicken.createAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void die(DamageSource damageSource) {
        if (!this.level().isClientSide) {
            MutableComponent message = this.getDisplayName().copy();
            message.append(Component.literal(" died!"));
            this.level().getServer().getPlayerList().broadcastSystemMessage(message, false);
        }
        super.die(damageSource);
    }
}