package com.hbm.handler.abilities;

import com.hbm.util.ContaminationUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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

    IWeaponAbility[] abilities = { NONE, RADIATION, VAMPIRE, STUN, /*PHOSPHORUS, FIRE, CHAINSAW, BEHEADER, BOBBLE*/ };

    static IWeaponAbility getByName(String name) {
        for(IWeaponAbility ability : abilities) {
            if(ability.getName().equals(name))
                return ability;
        }

        return NONE;
    }
}
