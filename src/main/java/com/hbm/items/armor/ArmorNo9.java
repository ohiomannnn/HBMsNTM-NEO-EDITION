package com.hbm.items.armor;

import com.hbm.extprop.HbmLivingAttachments;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.render.model.armor.ModelNo9;
import com.hbm.util.TagsUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.function.Consumer;

public class ArmorNo9 extends ArmorItem {

    public ArmorNo9(Holder<ArmorMaterial> material) {
        super(material, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.literal("+0.5 DT").withStyle(ChatFormatting.BLUE));
        components.add(Component.literal("Lets you breathe coal, neat!").withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private ModelNo9 replacement;

            @Override
            public Model getGenericArmorModel(LivingEntity living, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> original) {
                if(replacement == null) replacement = new ModelNo9(original, slot);
                replacement.getPropertiesFrom(original);
                replacement.living = living;
                return replacement;
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {

        if(!(entity instanceof Player player)) return;
        if(player.getItemBySlot(EquipmentSlot.HEAD) != stack) return;

        if(!level.isClientSide) {
            boolean turnOn = HbmPlayerAttachments.getData(player).enableHUD;
            CompoundTag tag = TagsUtil.getCustomData(stack);
            boolean wasOn = tag.getBoolean("isOn");

            if(turnOn && !wasOn) level.playSound(null, player, SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1F, 1.5F);
            if(!turnOn && wasOn) level.playSound(null, player, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5F, 2F);
            tag.putBoolean("isOn", turnOn);
            TagsUtil.putCustomData(stack, tag); // a crude way of syncing the "enableHUD" prop to other players is just by piggybacking off the NBT sync

            if(HbmLivingAttachments.getBlackLung(player) > HbmLivingAttachments.maxBlacklung * 0.9) {
                HbmLivingAttachments.setBlackLung(player, (int) (HbmLivingAttachments.maxBlacklung * 0.9));
            }
            if(HbmLivingAttachments.getBlackLung(player) >= HbmLivingAttachments.maxBlacklung * 0.25) {
                HbmLivingAttachments.setBlackLung(player, HbmLivingAttachments.getBlackLung(player) - 1);
            }
        }
    }
}
