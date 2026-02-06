package com.hbm.blocks.generic;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.blocks.ModBlocks;
import com.hbm.lib.ModSounds;
import com.hbm.util.EnumUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class PlushieBlock extends Block implements EntityBlock, ITooltipProvider {

    public static final IntegerProperty DIRECTION = IntegerProperty.create("direction", 0, 16);

    public PlushieType type;

    public PlushieBlock(Properties properties, PlushieType type) {
        super(properties);
        this.type = type;
        this.registerDefaultState(((this.stateDefinition.any()).setValue(DIRECTION, 0)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(DIRECTION, Mth.floor((double) ((context.getRotation() + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (this == ModBlocks.PLUSHIE_YOMI.get()) return PlushieBlockEntity.yomi(pos, state);
        if (this == ModBlocks.PLUSHIE_NUMBERNINE.get()) return PlushieBlockEntity.numbernine(pos, state);
        if (this == ModBlocks.PLUSHIE_HUNDUN.get()) return PlushieBlockEntity.hundun(pos, state);
        if (this == ModBlocks.PLUSHIE_DERG.get()) return PlushieBlockEntity.derg(pos, state);
        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        PlushieBlockEntity be = (PlushieBlockEntity) level.getBlockEntity(pos);

        assert be != null;
        if (level.isClientSide) {
            be.squishTimer = 11;
        } else {
            if (be.type == PlushieType.HUNDUN) {
                level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ModSounds.HUNDUS.get(), SoundSource.BLOCKS, 100F, 1F);
            } else {
                level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ModSounds.SQUEAKY_TOY.get(), SoundSource.BLOCKS, 100F, 1F);
            }
        }

        return InteractionResult.SUCCESS;
    }

    public static class PlushieBlockEntity extends BlockEntity {

        public PlushieType type;
        public int squishTimer;

        public void updateEntity() {
            if (squishTimer > 0) squishTimer--;
        }

        public static void tick(PlushieBlockEntity be) { be.updateEntity(); }

        public PlushieBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, PlushieType pType) {
            super(type, pos, blockState);
            this.type = pType;
        }

        public static PlushieBlockEntity yomi(BlockPos pos, BlockState state) {
            return new PlushieBlockEntity(ModBlockEntities.PLUSHIE_YOMI.get(), pos, state, PlushieType.YOMI);
        }

        public static PlushieBlockEntity numbernine(BlockPos pos, BlockState state) {
            return new PlushieBlockEntity(ModBlockEntities.PLUSHIE_NUMBERNINE.get(), pos, state, PlushieType.NUMBERNINE);
        }

        public static PlushieBlockEntity hundun(BlockPos pos, BlockState state) {
            return new PlushieBlockEntity(ModBlockEntities.PLUSHIE_HUNDUN.get(), pos, state, PlushieType.HUNDUN);
        }

        public static PlushieBlockEntity derg(BlockPos pos, BlockState state) {
            return new PlushieBlockEntity(ModBlockEntities.PLUSHIE_DERG.get(), pos, state, PlushieType.DERG);
        }

        @Override
        public Packet<ClientGamePacketListener> getUpdatePacket() {
            return ClientboundBlockEntityDataPacket.create(this);
        }

        @Override
        public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
            CompoundTag tag = new CompoundTag();
            this.saveAdditional(tag, registries);
            return tag;
        }

        @Override
        public void onDataPacket(Connection con, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
            CompoundTag tag = packet.getTag();
            this.loadAdditional(tag, registries);
        }

        @Override
        protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
            this.type = EnumUtil.grabEnumSafely(PlushieType.class, tag.getByte("Type"));
        }

        @Override
        protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
            tag.putByte("Type", (byte) this.type.ordinal());
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> {
            if (be instanceof PlushieBlockEntity tickable) PlushieBlockEntity.tick(tickable);
        };
    }

    public enum PlushieType {
        YOMI,
        NUMBERNINE,
        HUNDUN,
        DERG
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        for (String s : ITooltipProvider.getDescription(stack)) {
            components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
        }
    }
}
