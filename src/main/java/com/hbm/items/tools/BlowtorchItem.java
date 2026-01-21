package com.hbm.items.tools;

import api.hbm.block.IToolable;
import api.hbm.block.IToolable.ToolType;
import api.hbm.fluidmk2.IFillableItem;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.util.TagsUtilDegradation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Locale;

public class BlowtorchItem extends Item implements IFillableItem {

    public BlowtorchItem(Properties properties) {
        super(properties);

        ToolType.TORCH.register(new ItemStack(this));
    }

    @Override
    public boolean acceptsFluid(FluidType type, ItemStack stack) {

        if (this == ModItems.BLOWTORCH.get()) return type == Fluids.GAS;
        if (this == ModItems.ACETYLENE_TORCH.get()) return type == Fluids.UNSATURATEDS || type == Fluids.OXYGEN;

        return false;
    }

    @Override
    public int tryFill(FluidType type, int amount, ItemStack stack) {

        if (!this.acceptsFluid(type, stack)) return amount;

        int toFill = Math.min(amount, 50);
        toFill = Math.min(toFill, getMaxFill(type) - this.getFill(stack, type));
        this.setFill(stack, type, this.getFill(stack, type) + toFill);

        return amount - toFill;
    }

    public int getFill(ItemStack stack, FluidType type) {
        if (!TagsUtilDegradation.containsAnyTag(stack)) {
            initNBT(stack);
        }

        CompoundTag tag = TagsUtilDegradation.getTag(stack);

        //just in case
        String name = Fluids.toNameCompat(type);
        if (tag.contains(name)) {
            int fill = tag.getInt(name);
            tag.remove(name);
            tag.putInt(Integer.toString(type.getID()), fill);
            TagsUtilDegradation.putTag(stack, tag);

            return fill;
        }

        return tag.getInt(Integer.toString(type.getID()));
    }

    public int getMaxFill(FluidType type) {
        if (type == Fluids.GAS) return 4_000;
        if (type == Fluids.UNSATURATEDS) return 8_000;
        if (type == Fluids.OXYGEN) return 16_000;

        return 0;
    }

    public void setFill(ItemStack stack, FluidType type, int fill) {
        if (!TagsUtilDegradation.containsAnyTag(stack)) {
            initNBT(stack);
        }

        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        tag.putInt(Integer.toString(type.getID()), fill);
        TagsUtilDegradation.putTag(stack, tag);
    }

    public void initNBT(ItemStack stack) {

        TagsUtilDegradation.putTag(stack, new CompoundTag());

        if (this == ModItems.BLOWTORCH.get()) {
            this.setFill(stack, Fluids.GAS, this.getMaxFill(Fluids.GAS));
        }
        if (this == ModItems.ACETYLENE_TORCH.get()) {
            this.setFill(stack, Fluids.UNSATURATEDS, this.getMaxFill(Fluids.UNSATURATEDS));
            this.setFill(stack, Fluids.OXYGEN, this.getMaxFill(Fluids.OXYGEN));
        }
    }

    public static ItemStack getEmptyTool(Item item) {
        BlowtorchItem tool = (BlowtorchItem) item;
        ItemStack stack = new ItemStack(item);

        if (item == ModItems.BLOWTORCH.get()) {
            tool.setFill(stack, Fluids.GAS, 0);
        }
        if (item == ModItems.ACETYLENE_TORCH.get()) {
            tool.setFill(stack, Fluids.UNSATURATEDS, 0);
            tool.setFill(stack, Fluids.OXYGEN, 0);
        }

        return stack;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction clickedDir = context.getClickedFace();
        Vec3 clickedLocation = context.getClickLocation();
        ItemStack stack = context.getItemInHand();

        Block usedBlock = level.getBlockState(pos).getBlock();

        if (usedBlock instanceof IToolable it) {
            if (this == ModItems.BLOWTORCH.get()) {
                if (this.getFill(stack, Fluids.GAS) < 250) return InteractionResult.FAIL;
            }

            if (this == ModItems.ACETYLENE_TORCH.get()) {
                if (this.getFill(stack, Fluids.UNSATURATEDS) < 20) return InteractionResult.FAIL;
                if (this.getFill(stack, Fluids.OXYGEN) < 10) return InteractionResult.FAIL;
            }

            if (it.onScrew(level, player, pos, clickedDir, ToolType.TORCH)) {
                if (!level.isClientSide) {
                    if (this == ModItems.BLOWTORCH.get()) {
                        this.setFill(stack, Fluids.GAS, this.getFill(stack, Fluids.GAS) - 250);
                    }

                    if (this == ModItems.ACETYLENE_TORCH.get()) {
                        this.setFill(stack, Fluids.UNSATURATEDS, this.getFill(stack, Fluids.UNSATURATEDS) - 20);
                        this.setFill(stack, Fluids.OXYGEN, this.getFill(stack, Fluids.OXYGEN) - 10);
                    }
                }

                CompoundTag tag = new CompoundTag();
                tag.putString("type", "tau");
                tag.putByte("count", (byte) 10);
                if (level instanceof ServerLevel serverLevel) {
                    PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 50, new AuxParticle(tag, clickedLocation.x, clickedLocation.y, clickedLocation.z));
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private float getFillFraction(ItemStack stack) {
        if (this == ModItems.BLOWTORCH.get()) {
            return (float) this.getFill(stack, Fluids.GAS) / (float) this.getMaxFill(Fluids.GAS);
        }

        if (this == ModItems.ACETYLENE_TORCH.get()) {
            return Math.min(
                    (float) this.getFill(stack, Fluids.UNSATURATEDS) / (float) this.getMaxFill(Fluids.UNSATURATEDS),
                    (float) this.getFill(stack, Fluids.OXYGEN) / (float) this.getMaxFill(Fluids.OXYGEN)
            );
        }

        return 1.0F;
    }

    @Override public boolean isBarVisible(ItemStack stack) { return getFillFraction(stack) < 1.0F; }
    @Override public int getBarWidth(ItemStack stack) { return Math.round(13.0F * getFillFraction(stack)); }
    @Override public int getBarColor(ItemStack stack) { return Mth.hsvToRgb(this.getFillFraction(stack) / 3.0F, 1.0F, 1.0F); }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {

        if (this == ModItems.BLOWTORCH.get()) {
            components.add(Component.literal(this.getFillGauge(stack, Fluids.GAS)).withStyle(ChatFormatting.YELLOW));
        }

        if (this == ModItems.ACETYLENE_TORCH.get()) {
            components.add(Component.literal(this.getFillGauge(stack, Fluids.UNSATURATEDS)).withStyle(ChatFormatting.YELLOW));
            components.add(Component.literal(this.getFillGauge(stack, Fluids.OXYGEN)).withStyle(ChatFormatting.AQUA));
        }
    }

    private String getFillGauge(ItemStack stack, FluidType type) {
        return type.getLocalizedName() + ": " + String.format(Locale.US, "%,d", this.getFill(stack, type)) + " / " + String.format(Locale.US, "%,d", this.getMaxFill(type));
    }

    @Override public boolean providesFluid(FluidType type, ItemStack stack) { return false; }
    @Override public int tryEmpty(FluidType type, int amount, ItemStack stack) { return amount; }

    @Override public FluidType getFirstFluidType(ItemStack stack) { return null; }

    @Override public int getFill(ItemStack stack) { return 0; }
}
