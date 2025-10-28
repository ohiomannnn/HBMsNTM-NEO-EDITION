package com.hbm.blocks.bomb;

import com.hbm.CommonEvents;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.EntityProcessorCrossSmooth;
import com.hbm.explosion.vanillant.standard.PlayerProcessorStandard;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.interfaces.IBomb;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.util.DamageResistanceHandler;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public class LandmineBlock extends BaseEntityBlock implements IBomb {

    public static boolean safeMode = false;

    public double range;
    public double height;

    public static final MapCodec<LandmineBlock> CODEC = simpleCodec(LandmineBlock::new);

    protected LandmineBlock(Properties properties) {
        super(properties);
    }

    public LandmineBlock(Properties properties, double range, double height) {
        this(properties);
        this.range = range;
        this.height = height;
    }

    @Override
    public BombReturnCode explode(Level level, int x, int y, int z) {

        if (!level.isClientSide) {
            ExplosionVNT vnt = new ExplosionVNT(level, x + 0.5, y + 0.5, z + 0.5, 10);
            vnt.setBlockAllocator(new BlockAllocatorStandard(64));
            vnt.setBlockProcessor(new BlockProcessorStandard());
            vnt.setEntityProcessor(new EntityProcessorCrossSmooth(2, 100F).setDamageClass(DamageResistanceHandler.DamageClass.IN_FIRE).withRangeMod(1.5F));
            vnt.setPlayerProcessor(new PlayerProcessorStandard());
            vnt.explode();

            incrementRad(level, x, y, z, 1.5F);

            spawnMush(level, x, y, z, CommonEvents.polaroidID == 11 || level.random.nextInt(100) == 0);
        }

        return BombReturnCode.DETONATED;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    public static void spawnMush(Level level, int x, int y, int z, boolean balefire) {
        level.playSound(null,x + 0.5, y + 0.5, z + 0.5, ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 25.0F, 0.9F);
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "muke");
        tag.putBoolean("balefire", balefire);
        PacketDistributor.sendToPlayersNear(
                (ServerLevel) level,
                null,
                x + 0.5, y + 0.5, z + 0.5,
                250,
                new AuxParticle(tag, x + 0.5, y + 0.5, z + 0.5)
        );
    }

    public static void incrementRad(Level level, double posX, double posY, double posZ, float mult) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (Math.abs(i) + Math.abs(j) < 4) {
                    ChunkRadiationManager.proxy.incrementRad(level, (int) Math.floor(posX + i * 16), (int) Math.floor(posY), (int) Math.floor(posZ + j * 16), 50F / (Math.abs(i) + Math.abs(j) + 1) * mult);
                }
            }
        }
    }
}
