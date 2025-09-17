package com.hbm.blocks.generic;

import com.hbm.blockentity.machine.storage.CrateIronBlockEntity;
import com.hbm.config.ServerConfig;
import com.hbm.blockentity.machine.storage.CrateBaseBlockEntity;
import com.hbm.inventory.ModMenus;
import com.hbm.util.TagsUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

public class StorageCrateBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private final boolean isSafe;

    public StorageCrateBlock(Properties properties, boolean isSafe) {
        super(properties);
        this.isSafe = isSafe;
        if (isSafe) {
            this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        }
    }

    public StorageCrateBlock(Properties properties) {
        this(properties, false);
    }

    public static final MapCodec<StorageCrateBlock> CODEC = simpleCodec(StorageCrateBlock::new);

    @Override
    protected MapCodec<? extends StorageCrateBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrateIronBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CrateBaseBlockEntity crate) {
                if (crate.canAccess(player)) {
                    if (player instanceof ServerPlayer player1) {
                        player1.openMenu(crate, pos);
                        CrateBaseBlockEntity.spawnSpiders(player, level, crate);
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (isSafe && placer != null) {
            level.setBlock(pos, state.setValue(FACING, placer.getDirection().getOpposite()), 2);
        }

        if (TagsUtil.hasTag(stack)) {
            CompoundTag tag = TagsUtil.getTag(stack);
            if (tag.contains("BlockEntityTag")) {
                CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntity.loadCustomOnly(blockEntityTag, level.registryAccess());

                    if (blockEntity instanceof CrateBaseBlockEntity crate) {
                        crate.setCustomName(String.valueOf(stack.getDisplayName()));
                    }
                }
            }
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof CrateBaseBlockEntity lockable && !level.isClientSide && !player.isCreative()) {
            boolean keepContents = ServerConfig.CRATE_KEEP_CONTENTS.get();
            boolean isLocked = lockable.isLocked();

            if (!keepContents && !isLocked) {
                return super.playerWillDestroy(level, pos, state, player);
            }

            ItemStack drop = new ItemStack(this);
            CompoundTag blockEntityTag = blockEntity.saveWithFullMetadata(level.registryAccess());

            blockEntityTag.remove("x");
            blockEntityTag.remove("y");
            blockEntityTag.remove("z");

            if (!blockEntityTag.isEmpty()) {
                try {
                    ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                    try (GZIPOutputStream gzipoutputstream = new GZIPOutputStream(bytearrayoutputstream)) {
                        DataOutputStream dataOutputStream = new DataOutputStream(gzipoutputstream);
                        NbtIo.write(blockEntityTag, dataOutputStream);
                    }

                    if (bytearrayoutputstream.size() > 6000) {
                        player.sendSystemMessage(Component.literal("Warning: Container NBT exceeds 6kB, contents will be ejected!").withStyle(ChatFormatting.RED));
                    } else {
                        drop.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityTag));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            popResource(level, pos, drop);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CrateBaseBlockEntity crate) {

                boolean keepContents = ServerConfig.CRATE_KEEP_CONTENTS.get();
                boolean isLocked = crate.isLocked();

                if (!keepContents || isLocked) {
                    Containers.dropContents(level, pos, crate);
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        CompoundTag blockEntityTag = TagsUtil.getTagElement(stack, "BlockEntityTag");

        if (blockEntityTag != null) {
            if (blockEntityTag.getBoolean("spiders")) {
                if (blockEntityTag.contains("lock")) {
                    tooltip.add(Component.literal("This container is locked.").withStyle(ChatFormatting.RED));
                }
                tooltip.add(Component.literal("Skittering emanates from within...").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                return;
            }

            if (blockEntityTag.contains("lock")) {
                tooltip.add(Component.literal("This container is locked.").withStyle(ChatFormatting.RED));
                if (blockEntityTag.contains("Items") && !blockEntityTag.getList("Items", 10).isEmpty()) {
                    tooltip.add(Component.literal("It feels heavy...").withStyle(ChatFormatting.YELLOW));
                } else {
                    tooltip.add(Component.literal("It feels empty...").withStyle(ChatFormatting.YELLOW));
                }
                return;
            }

            if (blockEntityTag.contains("Items")) {
                var items = blockEntityTag.getList("Items", 10);
                if (!items.isEmpty()) {
                    tooltip.add(Component.literal("Contains:").withStyle(ChatFormatting.AQUA));
                    int limit = Math.min(items.size(), 10);
                    for (int i = 0; i < limit; i++) {
                        CompoundTag itemTag = items.getCompound(i);
                        ItemStack contentStack = ItemStack.parse(Objects.requireNonNull(context.registries()), itemTag).orElse(ItemStack.EMPTY);
                        tooltip.add(Component.literal(" - ").append(contentStack.getHoverName()).append(" x" + contentStack.getCount()).withStyle(ChatFormatting.AQUA));
                    }
                    if (items.size() > limit) {
                        tooltip.add(Component.literal("...and " + (items.size() - limit) + " more.").withStyle(ChatFormatting.AQUA));
                    }
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        if (isSafe) {
            builder.add(FACING);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (isSafe) {
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        }
        return super.getStateForPlacement(context);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CrateBaseBlockEntity crate) {
            return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(crate);
        }
        return 0;
    }

}