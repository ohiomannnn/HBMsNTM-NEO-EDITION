package com.hbm.blocks.bomb;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.hbm.blocks.MultiBlock;
import com.hbm.config.NtmConfig;
import com.hbm.entity.logic.NukeExplosionBalefire;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.EntityProcessorCross;
import com.hbm.handler.compat.SableCompat;
import com.hbm.interfaces.IBomb;
import com.hbm.inventory.MetaHelper;
import com.hbm.items.special.PolaroidItem;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.particle.helper.ExplosionCreator;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.EnumUtil;
import com.hbm.util.SoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.BiConsumer;

public class CrashedBombBlock extends MultiBlock implements EntityBlock, IBomb {

    public enum DudType {
        BALEFIRE(LAMBDA_BALEFIRE),
        CONVENTIONAL(LAMBDA_CONVENTIONAL),
        NUKE(LAMBDA_NUKE),
        SALTED(LAMBDA_SALTED);

        public final BiConsumer<Level, BlockPos> explode;

        DudType(BiConsumer<Level, BlockPos> lambda) {
            this.explode = lambda;
        }
    }

    public static final BiConsumer<Level, BlockPos> LAMBDA_BALEFIRE = (level, pos) -> {
        NukeExplosionBalefire balefire = new NukeExplosionBalefire(level);
        balefire.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        balefire.destructionRange = (int) (NtmConfig.COMMON.FATMAN_RADIUS.get() * 1.25);
        level.addFreshEntity(balefire);
        spawnMush(level, pos, true);
    };
    public static final BiConsumer<Level, BlockPos> LAMBDA_CONVENTIONAL = (level, pos) -> {
        ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 35F)
                .setBlockAllocator(new BlockAllocatorStandard(24))
                .setBlockProcessor(new BlockProcessorStandard().setNoDrop())
                .setEntityProcessor(new EntityProcessorCross(5D).withRangeMod(1.5F));
        ExplosionCreator.composeEffectLarge(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        vnt.explode();
    };
    public static final BiConsumer<Level, BlockPos> LAMBDA_NUKE = (level, pos) -> {
        NukeExplosionMK5.statFac(level, 35, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        spawnMush(level, pos, PolaroidItem.polaroidID == 11 || level.random.nextInt(100) == 0);
    };
    public static final BiConsumer<Level, BlockPos> LAMBDA_SALTED = (level, pos) -> {
        NukeExplosionMK5.statFac(level, 25, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).setMoreFallout(25);
        spawnMush(level, pos, PolaroidItem.polaroidID == 11 || level.random.nextInt(100) == 0);
    };

    public static void spawnMush(Level level, BlockPos pos, boolean balefire) {
        SoundUtils.playAtBlockPosC(level, pos, NtmSoundEvents.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 15.0F, 1.0F);
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "muke");
        tag.putBoolean("balefire", balefire);
        if(level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 250, new AuxParticle(tag, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
        }
    }

    public static final IntegerProperty SUBTYPE = IntegerProperty.create("subtype", 0, DudType.values().length);

    public CrashedBombBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(SUBTYPE, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SUBTYPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(SUBTYPE, MetaHelper.getMeta(context.getItemInHand()));
    }

    @Override public int getMeta(BlockState state) { return state.getValue(SUBTYPE); }
    @Override public int getSubCount() { return DudType.values().length; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrashedBombBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> { if (be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {
        if(!level.isClientSide) {
            DudType type = EnumUtil.grabEnumSafely(DudType.class, this.getMeta(level.getBlockState(pos)));

            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            if(ModList.get().isLoaded("sable")) pos = SableCompat.getProj(level, pos);

            type.explode.accept(level, pos);
        }
        return BombReturnCode.DETONATED;
    }
}
