package com.hbm.items.tools;

import com.hbm.handler.ability.AvailableAbilities;
import com.hbm.handler.ability.IWeaponAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class SwordAbilityItem extends SwordItem {

    private AvailableAbilities abilities = new AvailableAbilities();

    public SwordAbilityItem(Properties properties, Tier tier, float damage, float attackSpeed) {
        super(tier, properties.attributes(ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (damage + tier.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build()));
    }

    public SwordAbilityItem addAbility(IWeaponAbility weaponAbility, int level) {
        this.abilities.addAbility(weaponAbility, level);
        return this;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) {

        if (!attacker.level().isClientSide && attacker instanceof Player player && canOperate(stack)) {
            this.abilities.getWeaponAbilities().forEach((ability, level) ->
                    ability.onHit(level, attacker.level(), player, victim, this));
        }

        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        abilities.appendHoverText(tooltipComponents);
    }

    protected boolean canOperate(ItemStack stack) {
        return true;
    }
}
