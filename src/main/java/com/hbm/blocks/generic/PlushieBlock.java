package com.hbm.blocks.generic;

import com.hbm.blockentity.BlockEntityNT;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.blocks.MultiBlock;
import com.hbm.blocks.generic.BarbedWireBlock.BarbedWireType;
import com.hbm.inventory.MetaHelper;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.EnumUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import java.util.Locale;

public class PlushieBlock extends MultiBlock implements EntityBlock, ITooltipProvider {

    public enum PlushieType {
        YOMI,
        NUMBERNINE,
        HUNDUN,
        DERG // blerg
    }

    public static final IntegerProperty SUBTYPE = IntegerProperty.create("subtype", 0, PlushieType.values().length - 1);
    public static final IntegerProperty DIRECTION = IntegerProperty.create("direction", 0, 16);

    public PlushieBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(SUBTYPE, 0)
                .setValue(DIRECTION, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SUBTYPE, DIRECTION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(SUBTYPE, MetaHelper.getMeta(context.getItemInHand()))
                .setValue(DIRECTION, Mth.floor((double) ((context.getRotation() + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15);
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlushieBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> { if (be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        BlockEntity be = level.getBlockEntity(pos);

        if(be instanceof PlushieBlockEntity plushie) {
            PlushieType type = EnumUtil.grabEnumSafely(PlushieType.class, this.getMeta(state));

            if(level.isClientSide) {
                plushie.squishTimer = 11;
            } else {
                if(type == PlushieType.HUNDUN) {
                    level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, NtmSoundEvents.HUNDUNS_MAGNIFICENT_HOWL.get(), SoundSource.BLOCKS, 100F, 1F);
                } else {
                    level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, NtmSoundEvents.SQUEAKY_TOY.get(), SoundSource.BLOCKS, 100F, 1F);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public String getItemDescriptionId(ItemStack stack) {
        Enum<?> num = EnumUtil.grabEnumSafely(PlushieType.class, MetaHelper.getMeta(stack));
        return super.getDescriptionId() + "." + num.name().toLowerCase(Locale.US);
    }

    @Override public int getMeta(BlockState state) { return state.getValue(SUBTYPE); }
    @Override public int getSubCount() { return PlushieType.values().length; }

    public static class PlushieBlockEntity extends BlockEntityNT implements ITickable {

        public int squishTimer;

        public PlushieBlockEntity(BlockPos pos, BlockState state) {
            super(NtmBlockEntityTypes.PLUSHIE.get(), pos, state);
        }

        @Override
        public void updateEntity() {
            if(squishTimer > 0) squishTimer--;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        for(String s : ITooltipProvider.getDescription(stack)) {
            components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
        }
    }
}
