package com.hbm.blockentity;

import com.hbm.HBMsNTMClient;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.items.tools.BlowtorchItem;
import com.hbm.util.InventoryUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface IRepairable {
    boolean isDamaged();
    List<AStack> getRepairMaterials();
    void repair();

    @Nullable
    static List<AStack> getRepairMaterials(Level level, BlockPos pos, DummyableBlock dummy, Player player) {
        List<ItemStack> items = InventoryUtil.getItemsFromBothHands(player);

        for (ItemStack stack : items) {
            if (!(stack.getItem() instanceof BlowtorchItem)) return null;

            BlockPos corePos = dummy.findCore(level, pos);
            if (corePos == null) return null;
            BlockEntity core = level.getBlockEntity(corePos);
            if (!(core instanceof IRepairable ir)) return null;

            if (!ir.isDamaged()) return null;
            return ir.getRepairMaterials();
        }

        return null;
    }

    static boolean tryRepairMultiblock(Level level, BlockPos pos, DummyableBlock dummy, Player player) {

        BlockPos corePos = dummy.findCore(level, pos);
        if (corePos == null) return false;
        BlockEntity core = level.getBlockEntity(corePos);
        if (!(core instanceof IRepairable ir)) return false;

        if (!ir.isDamaged()) return false;

        List<AStack> list = ir.getRepairMaterials();
        if (list == null || list.isEmpty() || InventoryUtil.doesPlayerHaveAStacks(player, list, true)) {
            if (!level.isClientSide) ir.repair();
            return true;
        }

        return false;
    }

    @OnlyIn(Dist.CLIENT)
    static void addGenericOverlay(RenderGuiEvent.Pre event, Level level, BlockPos pos, DummyableBlock dummy) {

        List<AStack> materials = IRepairable.getRepairMaterials(level, pos, dummy, HBMsNTMClient.me());
        if (materials == null) return;

        List<Component> text = new ArrayList<>();
        text.add(Component.translatable("overlay.repair_with").withStyle(ChatFormatting.GOLD));

        for (AStack stack : materials) {
            ItemStack display = stack.extractForCyclingDisplay(20);
            text.add(Component.literal("- " + display.getDisplayName().getString() + " x" + display.getCount()));
        }

        ILookOverlay.printGeneric(event, dummy.getName(), 0xffff00, 0x404000, text);
    }

    void tryExtinguish(Level level, BlockPos pos, EnumExtinguishType type);

    enum EnumExtinguishType {
        WATER,
        FOAM,
        SAND,
        CO2
    }
}
