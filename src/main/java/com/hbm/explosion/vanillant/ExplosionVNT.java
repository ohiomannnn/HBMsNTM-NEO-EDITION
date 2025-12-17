package com.hbm.explosion.vanillant;

import com.hbm.explosion.vanillant.interfaces.IBlockAllocator;
import com.hbm.explosion.vanillant.interfaces.IBlockProcessor;
import com.hbm.explosion.vanillant.interfaces.IEntityProcessor;
import com.hbm.explosion.vanillant.interfaces.IExplosionSFX;
import com.hbm.explosion.vanillant.standard.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ExplosionVNT {

    //explosions only need one of these, in the unlikely event that we do need to combine different types we can just write a wrapper that acts as a chainloader
    private IBlockAllocator blockAllocator;
    private IEntityProcessor entityProcessor;
    private IBlockProcessor blockProcessor;
    //since we want to reduce each effect to the bare minimum (sound, particles, etc. being separate) we definitely need multiple most of the time
    private IExplosionSFX[] sfx;

    public Level level;
    public double x;
    public double y;
    public double z;
    public float size;
    @Nullable public Entity exploder;

    // things for compatibility with vanilla
    private final Map<Player, Vec3> compatPlayers = new HashMap<>();
    public Explosion compat;

    public ExplosionVNT(Level level, double x, double y, double z, float size) {
        this(level, x, y, z, size, null);
    }

    public ExplosionVNT(Level level, double x, double y, double z, float size, @Nullable Entity exploder) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.exploder = exploder;

        this.compat = new Explosion(level, exploder, x, y, z, size, false, null) {

            @Override
            public Map<Player, Vec3> getHitPlayers() {
                return ExplosionVNT.this.compatPlayers;
            }

            @Override
            public Entity getDirectSourceEntity() {
                return ExplosionVNT.this.exploder;
            }

            @Override
            public float radius() {
                return ExplosionVNT.this.size;
            }
        };
    }

    public void explode() {

        boolean processBlocks = blockAllocator != null && blockProcessor != null;
        boolean processEntities = entityProcessor != null;

        HashSet<BlockPos> affectedBlocks = null;
        HashMap<Player, Vec3> affectedPlayers = null;

        //allocation
        if (processBlocks) affectedBlocks = blockAllocator.allocate(this, level, x, y, z, size);
        if (processBlocks) this.compat.getToBlow().addAll(affectedBlocks);
        if (processEntities) affectedPlayers = entityProcessor.process(this, level, x, y, z, size);
        // technically not necessary, as the affected entity list is a separate parameter during the Detonate event
        if (processEntities) this.compat.getHitPlayers().putAll(affectedPlayers);

        //serverside processing
        if (processBlocks) blockProcessor.process(this, level, x, y, z, affectedBlocks);

        //from server to client
        if (sfx != null) {
            for (IExplosionSFX fx : sfx) {
                fx.doEffect(this, level, x, y, z, size);
            }
        }

    }

    public ExplosionVNT setBlockAllocator(IBlockAllocator blockAllocator) {
        this.blockAllocator = blockAllocator;
        return this;
    }

    public ExplosionVNT setEntityProcessor(IEntityProcessor entityProcessor) {
        this.entityProcessor = entityProcessor;
        return this;
    }

    public ExplosionVNT setBlockProcessor(IBlockProcessor blockProcessor) {
        this.blockProcessor = blockProcessor;
        return this;
    }

    public ExplosionVNT setSFX(IExplosionSFX... sfx) {
        this.sfx = sfx;
        return this;
    }

    public ExplosionVNT makeStandard() {
        this.setBlockAllocator(new BlockAllocatorStandard());
        this.setBlockProcessor(new BlockProcessorStandard());
        this.setEntityProcessor(new EntityProcessorStandard());
        this.setSFX(new ExplosionEffectStandard());
        return this;
    }

    public ExplosionVNT makeAmat() {
        this.setBlockAllocator(new BlockAllocatorStandard(this.size < 15 ? 16 : 32));
        this.setBlockProcessor(new BlockProcessorStandard().setNoDrop());
        this.setEntityProcessor(new EntityProcessorStandard().withRangeMod(2.0F).withDamageMod(new CustomDamageHandlerRadiation(50.0F)));
        this.setSFX(new ExplosionEffectAmat());
        return this;
    }
}
