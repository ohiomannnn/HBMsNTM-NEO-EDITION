package com.hbm.blocks.machine;

import api.hbm.block.IToolable;
import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.IRepairable;
import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.storage.MachineFluidTankBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.util.InventoryUtil;
import com.hbm.util.TagsUtilDegradation;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.List;

public class MachineFluidTankBlock extends DummyableBlock implements IToolable, ILookOverlay {

    public MachineFluidTankBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch (type) {
            case CORE -> new MachineFluidTankBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return BaseEntityBlock.createTickerHelper(type, ModBlockEntityTypes.FLUID_TANK.get(), MachineFluidTankBlockEntity::tick);
    }

    public static final MapCodec<MachineFluidTankBlock> CODEC = simpleCodec(MachineFluidTankBlock::new);
    @Override public MapCodec<MachineFluidTankBlock> codec() { return CODEC; }

    @Override public int[] getDimensions() { return new int[] { 2, 0, 1, 1, 2, 2 }; }
    @Override public int getOffset() { return 1; }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        this.makeExtra(level, new BlockPos(x - dir.getStepX() + 1, y, z - dir.getStepZ() + 1));
        this.makeExtra(level, new BlockPos(x - dir.getStepX() + 1, y, z - dir.getStepZ() - 1));
        this.makeExtra(level, new BlockPos(x - dir.getStepX() - 1, y, z - dir.getStepZ() + 1));
        this.makeExtra(level, new BlockPos(x - dir.getStepX() - 1, y, z - dir.getStepZ() - 1));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        CompoundTag persistent = TagsUtilDegradation.getTag(stack).getCompound("persistent");
        FluidTank tank = new FluidTank(Fluids.NONE, 0);
        if (persistent.contains("Tank")) {
            tank.readFromNBT(persistent, "Tank");
            components.add(Component.translatable("fluid.info.mb.name", tank.getFill(), tank.getMaxFill(), tank.getTankType().getName()).withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos corePos = this.findCore(level, pos);
        if (corePos == null) {
            return InteractionResult.FAIL;
        }

        if (!player.isShiftKeyDown()) {
            BlockEntity blockentity = level.getBlockEntity(corePos);
            if (blockentity instanceof MenuProvider be) {
                player.openMenu(new SimpleMenuProvider(be, be.getDisplayName()), pos);
            }
            return InteractionResult.CONSUME;
        } else {
            BlockEntity blockentity = level.getBlockEntity(corePos);
            if (blockentity instanceof MachineFluidTankBlockEntity be) {
                for (ItemStack stack : InventoryUtil.getItemsFromBothHands(player)) {
                    if (stack.getItem() instanceof IItemFluidIdentifier iifi) {
                        FluidType type = iifi.getType(level, corePos, stack);

                        be.tank.setTankType(type);
                        be.setChanged();
                        player.displayClientMessage(Component.translatable("block.hbmsntm.machine_fluid_tank.changed_type_to", type.getName()).withStyle(ChatFormatting.YELLOW), false);
                    }
                }

                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return IPersistentNBT.getDropsFromLootParams(state, params);
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return false;
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {

        BlockPos corePos = this.findCore(level, pos);
        if (corePos == null) return;
        BlockEntity core = level.getBlockEntity(corePos);
        if (!(core instanceof MachineFluidTankBlockEntity be)) return;
        if (be.lastExplosion == explosion) return;
        be.lastExplosion = explosion;

        if (!be.hasExploded) {
            be.explode();
        } else {
            level.setBlock(corePos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    @Override
    public boolean onScrew(Level level, Player player, BlockPos pos, Direction direction, ToolType tool) {
        if (tool != ToolType.TORCH) return false;
        return IRepairable.tryRepairMultiblock(level, pos, this, player);
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        IRepairable.addGenericOverlay(event, level, pos, this);
    }
}
