package com.hbm.blocks.bomb;

import com.hbm.entity.item.TNTPrimedBase;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.explosion.ExplosionNukeGeneric;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockMutatorBalefire;
import com.hbm.explosion.vanillant.standard.BlockMutatorFire;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.items.special.PolaroidItem;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class FissureBombBlock extends TNTBaseBlock {

    public FissureBombBlock(Properties properties) { super(properties); }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, TNTPrimedBase entity) {

        // this has to be the single worst solution ever
        level.playSound(null, x, y, z, ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 25.0F, 0.9F);
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "muke");
        tag.putBoolean("balefire", PolaroidItem.polaroidID == 11 || level.random.nextInt(100) == 0);
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 250, new AuxParticle(tag, x, y, z));
        }
        if (level instanceof ServerLevel serverLevel) {
            ExplosionLarge.spawnShrapnels(serverLevel, x, y, z, 25);
        }
        ExplosionVNT vnt = new ExplosionVNT(level, x, y, z, 20F)
                .setBlockAllocator(new BlockAllocatorStandard(64))
                .setBlockProcessor(new BlockProcessorStandard().withBlockEffect(new BlockMutatorFire()));
        vnt.explode();

        ExplosionNukeGeneric.dealDamage(level, x, y, z, 55F);
        ExplosionNukeGeneric.incrementRad(level, x, y, z, 3 / 3F);
    }
}
