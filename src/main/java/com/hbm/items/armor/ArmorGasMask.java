package com.hbm.items.armor;

import com.hbm.HBMsNTM;
import com.hbm.render.model.ModelGasMask;
import com.hbm.util.ArmorUtil;
import com.hbm.util.ArmorRegistry.HazardClass;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmorGasMask extends ArmorItem {
    public ArmorGasMask(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

//    public static final ResourceLocation GAS_MASK_TEX = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/models/gas_mask.png");
//
//    public ArmorGasMask(Holder<ArmorMaterial> material, Type type, Properties properties) {
//        super(material, type, properties);
//    }
//
//    @Override
//    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
//        return GAS_MASK_TEX;
//    }
//
//    @Override
//    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
//        ArmorUtil.addGasMaskTooltip(stack, tooltipComponents);
//
//        List<HazardClass> haz = getBlacklist(stack);
//        if (!haz.isEmpty()) {
//            tooltipComponents.add(Component.translatable("hazard.neverProtects").withStyle(ChatFormatting.RED));
//            for (tooltipComponents clazz : haz) {
//                tooltip.add(Component.literal(" -" + I18nUtil.resolveKey(clazz.lang)).withStyle(ChatFormatting.DARK_RED));
//            }
//        }
//    }
//
//    // === Логика фильтров ===
//    public List<HazardClass> getBlacklist(ItemStack stack) {
//        // пример: моно-маска защищает хуже
//        return new ArrayList<>(Arrays.asList(HazardClass.GAS_BLISTERING));
//    }
//
//    public ItemStack getFilter(ItemStack stack) {
//        return ArmorUtil.getGasMaskFilter(stack);
//    }
//
//    public void installFilter(ItemStack stack, ItemStack filter) {
//        ArmorUtil.installGasMaskFilter(stack, filter);
//    }
//
//    public void damageFilter(ItemStack stack, int damage) {
//        ArmorUtil.damageGasMaskFilter(stack, damage);
//    }
//
//    // === Использование (правый клик) ===
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        ItemStack stack = player.getItemInHand(hand);
//        if (player.isShiftKeyDown()) {
//            ItemStack filter = this.getFilter(stack);
//            if (!filter.isEmpty()) {
//                ArmorUtil.removeFilter(stack);
//                if (!player.addItem(filter)) {
//                    player.drop(filter, true);
//                }
//                return InteractionResultHolder.success(stack);
//            }
//        }
//        return super.use(level, player, hand);
//    }
//
//    // === Оверлей на экран ===
//    public void renderHelmetOverlay(ItemStack stack, GuiGraphics guiGraphics, float partialTicks) {
//        // В новых версиях вместо GL11 используется GuiGraphics.blit
//        ResourceLocation overlay = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/misc/overlay_gasmask_0.png");
//        int w = guiGraphics.guiWidth();
//        int h = guiGraphics.guiHeight();
//        guiGraphics.blit(overlay, 0, 0, -90, 0, 0, w, h, w, h);
//    }
}
