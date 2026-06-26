package com.hbm.items.machine;

import com.hbm.blockentity.IScreenProvider;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.screens.FluidScreen;
import com.hbm.items.IItemControlReceiver;
import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toclient.InformPlayer;
import com.hbm.util.TagsUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class FluidIDMultiItem extends Item implements IScreenProvider, IItemControlReceiver, IItemFluidIdentifier {

    public FluidIDMultiItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (!level.isClientSide && !player.isShiftKeyDown()) {
            FluidType primary = getType(stack, true);
            FluidType secondary = getType(stack, false);
            setType(stack, secondary, true);
            setType(stack, primary, false);
            this.update(stack);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT, 0.25F, 1.25F);
            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new InformPlayer(secondary.getName(), 7, 3000));
            }
        }

        if(player.isShiftKeyDown()) {
            NuclearTechMod.proxy.openScreen(player, BlockPos.ZERO);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void receiveControl(ItemStack stack, CompoundTag tag) {
        if (tag.contains("Primary")) setType(stack, Fluids.fromID(tag.getInt("Primary")), true);
        if (tag.contains("Secondary")) setType(stack, Fluids.fromID(tag.getInt("Secondary")), false);

        this.update(stack);
    }

    public ItemStack update(ItemStack stack) {
        MetaHelper.setMeta(stack, TagsUtil.getCData(stack).getInt("Fluid1"));
        return stack;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("item.hbmsntm.obj_fluid_id_multi.desc0").withStyle(ChatFormatting.GRAY));
        components.add(Component.literal("   ").append(getType(stack, true).getName()).withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("item.hbmsntm.obj_fluid_id_multi.desc1").withStyle(ChatFormatting.GRAY));
        components.add(Component.literal("   ").append(getType(stack, false).getName()).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public FluidType getType(Level level, BlockPos pos, ItemStack stack) {
        return getType(stack, true);
    }

    public static void setType(ItemStack stack, FluidType type, boolean primary) {
        CompoundTag tag = TagsUtil.getCData(stack);
        tag.putInt("Fluid" + (primary ? 1 : 2), type.getID());
        TagsUtil.putCData(stack, tag);
    }

    public static FluidType getType(ItemStack stack, boolean primary) {
        if (!TagsUtil.hasCData(stack)) return Fluids.NONE;

        CompoundTag tag = TagsUtil.getCData(stack);
        int type = tag.getInt("Fluid" + (primary ? 1 : 2));
        return Fluids.fromID(type);
    }

    public static ItemStack createStack(FluidType type) {
        FluidIDMultiItem item = (FluidIDMultiItem) NtmItems.FLUID_IDENTIFIER_MULTI.get();
        ItemStack stack = new ItemStack(item);
        setType(stack, type, true);
        item.update(stack);
        return stack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Object provideScreen(Player player, BlockPos pos) {
        return new FluidScreen(player);
    }
}
