package com.hbm.items.machine;

import com.hbm.blockentity.IGUIProvider;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.screens.FluidScreen;
import com.hbm.items.IItemControlReceiver;
import com.hbm.items.ModItems;
import com.hbm.items.datacomps.FluidTypeComponent;
import com.hbm.items.datacomps.ModDataComponents;
import com.hbm.network.toclient.InformPlayer;
import com.hbm.util.TagsUtilDegradation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
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
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class FluidIDMultiItem extends Item implements IGUIProvider, IItemControlReceiver, IItemFluidIdentifier {

    public FluidIDMultiItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (!level.isClientSide && !player.isCrouching()) {
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
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public void receiveControl(ItemStack stack, CompoundTag tag) {
        if (tag.contains("Primary")) {
            setType(stack, Fluids.fromID(tag.getInt("Primary")), true);
        }
        if (tag.contains("Secondary")) {
            setType(stack, Fluids.fromID(tag.getInt("Secondary")), false);
        }

        this.update(stack);
    }

    public ItemStack update(ItemStack stack) {
        stack.set(ModDataComponents.FLUID_TYPE.get(), new FluidTypeComponent(TagsUtilDegradation.getTag(stack).getInt("Fluid1")));
        return stack;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable(this.getDescriptionId() + ".info").withStyle(ChatFormatting.GRAY));
        components.add(Component.literal("   ").append(getType(stack, true).getName()).withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable(this.getDescriptionId() + ".info2").withStyle(ChatFormatting.GRAY));
        components.add(Component.literal("   ").append(getType(stack, false).getName()).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public FluidType getType(Level level, BlockPos pos, ItemStack stack) {
        return getType(stack, true);
    }

    public static void setType(ItemStack stack, FluidType type, boolean primary) {
        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        tag.putInt("Fluid" + (primary ? 1 : 2), type.getID());
        TagsUtilDegradation.putTag(stack, tag);
    }

    public static FluidType getType(ItemStack stack, boolean primary) {
        if (!TagsUtilDegradation.containsAnyTag(stack)) return Fluids.NONE;

        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        int type = tag.getInt("Fluid" + (primary ? 1 : 2));
        return Fluids.fromID(type);
    }

    public static ItemStack createStack(FluidType type) {
        FluidIDMultiItem item = (FluidIDMultiItem) ModItems.FLUID_IDENTIFIER_MULTI.get();
        ItemStack stack = new ItemStack(item);
        setType(stack, type, true);
        item.update(stack);
        return stack;
    }

    @Override
    public Screen provideScreenOnRightClick(Player player, BlockPos pos) {
        if (player.isCrouching()) return new FluidScreen(player);;
        return null;
    }
}
