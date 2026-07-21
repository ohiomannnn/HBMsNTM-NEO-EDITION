package com.hbm.blocks.machine;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.machine.storage.CrateBaseBlockEntity;
import com.hbm.blockentity.machine.storage.CrateDeshBlockEntity;
import com.hbm.blockentity.machine.storage.CrateIronBlockEntity;
import com.hbm.blockentity.machine.storage.CrateSteelBlockEntity;
import com.hbm.blockentity.machine.storage.CrateTemplateBlockEntity;
import com.hbm.blockentity.machine.storage.CrateTungstenBlockEntity;
import com.hbm.config.NtmConfig;
import com.hbm.blocks.IPersistentInfoProvider;
import com.hbm.util.TagsUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class CrateBlock extends BaseEntityBlock implements IPersistentInfoProvider {

    public static final MapCodec<CrateBlock> CODEC = simpleCodec(properties -> new CrateBlock(properties, Type.IRON));
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final Type type;

    public CrateBlock(Properties properties, Type type) {
        super(properties);
        this.type = type;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, net.minecraft.core.Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return switch(this.type) {
            case IRON -> new CrateIronBlockEntity(pos, state);
            case TUNGSTEN -> new CrateTungstenBlockEntity(pos, state);
            case STEEL -> new CrateSteelBlockEntity(pos, state);
            case DESH -> new CrateDeshBlockEntity(pos, state);
            case TEMPLATE -> new CrateTemplateBlockEntity(pos, state);
        };
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if(!level.isClientSide) IPersistentNBT.restoreData(level, pos, stack);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof CrateBaseBlockEntity crate && crate.canAccess(player)) {
            CrateBaseBlockEntity.spawnSpiders(player, level, crate);
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
            if(blockEntity instanceof CrateBaseBlockEntity crate) {
                if(NtmConfig.SERVER.CRATE_KEEP_CONTENTS.get()) {
                    for(ItemStack stack : crate.getDrops(this)) Block.popResource(level, pos, stack);
                } else {
                    Containers.dropContents(level, pos, crate);
                    Block.popResource(level, pos, new ItemStack(this));
                }
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
        return IPersistentNBT.getDropsFromLootParams(state, params);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof CrateBaseBlockEntity crate) {
            CompoundTag tag = new CompoundTag();
            crate.writeNBT(tag);
            if(!tag.isEmpty()) TagsUtil.putCustomData(stack, tag);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        if(TagsUtil.getCustomData(stack).contains(IPersistentNBT.NBT_PERSISTENT_KEY)) return;
        this.addSlotsUsedTooltip(components, 0);
    }

    @Override
    public void appendHoverText(ItemStack stack, CompoundTag tag, List<Component> components, Item.TooltipContext context, TooltipFlag flag) {
        int occupiedSlots = 0;
        if(tag.contains("Items")) {
            var list = tag.getList("Items", 10);
            occupiedSlots = list.size();
        }

        this.addSlotsUsedTooltip(components, occupiedSlots);
    }

    private void addSlotsUsedTooltip(List<Component> components, int occupiedSlots) {
        double percent = this.type.slots == 0 ? 0.0D : occupiedSlots * 100.0D / this.type.slots;
        components.add(Component.translatable(
                "desc.crate_slots_used",
                occupiedSlots,
                this.type.slots,
                String.format(Locale.ROOT, "%.1f", percent)
        ).withStyle(this.getSlotsUsedColor(percent)));
    }

    private ChatFormatting getSlotsUsedColor(double percent) {
        if(percent >= 90.0D) return ChatFormatting.RED;
        if(percent >= 50.0D) return ChatFormatting.GOLD;
        if(percent > 0.0D) return ChatFormatting.YELLOW;
        return ChatFormatting.GREEN;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public enum Type {
        IRON(36),
        TUNGSTEN(27),
        STEEL(54),
        DESH(104),
        TEMPLATE(27);

        private final int slots;

        Type(int slots) {
            this.slots = slots;
        }
    }
}
