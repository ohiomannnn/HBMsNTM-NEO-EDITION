package com.hbm.blocks.machine;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.machine.storage.BarrelBlockEntity;
import com.hbm.blocks.IPersistentInfoProvider;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.util.InventoryUtil;
import com.hbm.util.TagsUtil;
import com.hbm.util.i18n.I18nUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.stats.Stats;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BarrelBlock extends BaseEntityBlock implements IPersistentInfoProvider {

    public static final MapCodec<BarrelBlock> CODEC = simpleCodec(props -> new BarrelBlock(props, 12_000, false));
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    private final int capacity;
    private final boolean corroded;

    public BarrelBlock(Properties properties, int capacity, boolean corroded) {
        super(properties);
        this.capacity = capacity;
        this.corroded = corroded;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isCorroded() {
        return this.corroded;
    }

    @Override
    public MapCodec<BarrelBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.corroded ? null : new BarrelBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if(!level.isClientSide) {
            IPersistentNBT.restoreData(level, pos, stack);
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(this.corroded) return null;
        return (lvl, pos, st, be) -> {
            if(be instanceof ITickable tickable) tickable.updateEntity();
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(this.corroded) return InteractionResult.PASS;
        if(level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof BarrelBlockEntity barrel) {
            if(player.isShiftKeyDown()) {
                for(ItemStack stack : InventoryUtil.getItemsFromBothHands(player)) {
                    if(stack.getItem() instanceof IItemFluidIdentifier identifier) {
                        FluidType type = identifier.getType(level, pos, stack);
                        barrel.tank.setTankType(type);
                        barrel.setChanged();
                        player.displayClientMessage(Component.translatable("block.hbmsntm.machine_fluid_tank.changed_type_to", type.getName()).withStyle(ChatFormatting.YELLOW), false);
                        return InteractionResult.CONSUME;
                    }
                }

                return InteractionResult.CONSUME;
            }

            if(blockEntity instanceof MenuProvider menu) {
                player.openMenu(new SimpleMenuProvider(menu, menu.getDisplayName()), pos);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(!level.isClientSide && !state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if(blockEntity instanceof BarrelBlockEntity barrel) {
                Containers.dropContents(level, pos, barrel);

                if(barrel.shouldDrop()) {
                    for(ItemStack stack : barrel.getDrops(this)) {
                        Block.popResource(level, pos, stack);
                    }
                }
            } else {
                Block.popResource(level, pos, new ItemStack(this));
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack tool) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if(this.corroded) return super.getDrops(state, params);
        return IPersistentNBT.getDropsFromLootParams(state, params);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("desc.capacity", String.format("%,d", this.capacity)).withStyle(ChatFormatting.AQUA));

        if(this.corroded) {
            components.add(Component.translatable("desc.canhot").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable("desc.canhighcor").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable("desc.cannotam").withStyle(ChatFormatting.YELLOW));
            components.add(Component.translatable("desc.leaky").withStyle(ChatFormatting.RED));
        } else if(this.capacity == 12_000) {
            components.add(Component.translatable("desc.cannothot").withStyle(ChatFormatting.YELLOW));
            components.add(Component.translatable("desc.cannotcor").withStyle(ChatFormatting.YELLOW));
            components.add(Component.translatable("desc.cannotam").withStyle(ChatFormatting.YELLOW));
        } else if(this.capacity == 16_000) {
            components.add(Component.translatable("desc.canhot").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable("desc.cancor").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable("desc.cannothighcor").withStyle(ChatFormatting.YELLOW));
            components.add(Component.translatable("desc.cannotam").withStyle(ChatFormatting.YELLOW));
        } else if(this.capacity == 24_000) {
            components.add(Component.translatable("desc.canhot").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable("desc.cannothighcor").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable("desc.cannotam").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, CompoundTag tag, List<Component> components, Item.TooltipContext context, TooltipFlag flag) {
        FluidTank tank = new FluidTank(Fluids.NONE, 0);
        if(tag.contains("tank")) {
            tank.readFromNBT(tag, "tank");
            components.add(Component.literal(tank.getFill() + "/" + tank.getMaxFill() + "mB " + tank.getTankType().getName().getString()).withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
