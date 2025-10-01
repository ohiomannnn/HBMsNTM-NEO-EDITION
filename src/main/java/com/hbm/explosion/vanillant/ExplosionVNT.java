package com.hbm.explosion.vanillant;

import com.hbm.explosion.vanillant.interfaces.*;
import com.hbm.explosion.vanillant.standard.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ExplosionVNT {

    //explosions only need one of these, in the unlikely event that we do need to combine different types we can just write a wrapper that acts as a chainloader
    private IBlockAllocator blockAllocator;
    private IEntityProcessor entityProcessor;
    private IBlockProcessor blockProcessor;
    private IPlayerProcessor playerProcessor;
    //since we want to reduce each effect to the bare minimum (sound, particles, etc. being separate) we definitely need multiple most of the time
    private IExplosionSFX[] sfx;

    public Level level;
    public double posX;
    public double posY;
    public double posZ;
    public float size;
    public Entity exploder;

    private final Map<Player, Vec3> hitPlayers = new HashMap<>();
    public Explosion compat;
    public Entity source;
    public float radius;

    public ExplosionVNT(Level level, double x, double y, double z, float size) {
        this(level, x, y, z, size, null);
    }

    public ExplosionVNT(Level level, double x, double y, double z, float size, Entity exploder) {
        this.level = level;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.size = size;
        this.exploder = exploder;

        this.compat = new Explosion(level, exploder, x, y, z, size, false, Explosion.BlockInteraction.DESTROY_WITH_DECAY) {

            @Override
            public Map<Player, Vec3> getHitPlayers() {
                return ExplosionVNT.this.hitPlayers;
            }

            @Override
            public Entity getDirectSourceEntity() {
                return ExplosionVNT.this.source;
            }

            @Override
            public float radius() {
                return ExplosionVNT.this.radius;
            }
        };
    }

    public void explode() {

        this.source = this.exploder;
        this.radius = this.size;

        boolean processBlocks = blockAllocator != null && blockProcessor != null;
        boolean processEntities = entityProcessor != null && playerProcessor != null;

        HashSet<BlockPos> affectedBlocks = null;
        Map<Player, Vec3> affectedPlayers = null;

        //allocation
        if (processBlocks) affectedBlocks = blockAllocator.allocate(this, level, posX, posY, posZ, size);
        if (processEntities) affectedPlayers = entityProcessor.processEntities(this, level, posX, posY, posZ, size);

        //serverside processing
        if (processBlocks) blockProcessor.process(this, level, posX, posY, posZ, affectedBlocks);
        if (processEntities) playerProcessor.processPlayers(this, level, posX, posY, posZ, affectedPlayers);

        if (sfx != null) {
            for (IExplosionSFX fx : sfx) {
                fx.doEffect(this, level, posX, posY, posZ, size);
            }
        }
    }

    public void setBlockAllocator(IBlockAllocator blockAllocator) {
        this.blockAllocator = blockAllocator;
    }

    public void setEntityProcessor(IEntityProcessor entityProcessor) {
        this.entityProcessor = entityProcessor;
    }

    public void setBlockProcessor(IBlockProcessor blockProcessor) {
        this.blockProcessor = blockProcessor;
    }

    public void setPlayerProcessor(IPlayerProcessor playerProcessor) {
        this.playerProcessor = playerProcessor;
    }

    public void setSFX(IExplosionSFX... sfx) {
        this.sfx = sfx;
    }

    public void makeStandard() {
        this.setBlockAllocator(new BlockAllocatorStandard());
        this.setBlockProcessor(new BlockProcessorStandard());
        this.setEntityProcessor(new EntityProcessorStandard());
        this.setPlayerProcessor(new PlayerProcessorStandard());
        this.setSFX(new ExplosionEffectStandard());
    }

    public void makeAmat() {
        this.setBlockAllocator(new BlockAllocatorStandard(this.size < 15 ? 16 : 32));
        this.setBlockProcessor(new BlockProcessorStandard()
                .setNoDrop());
        this.setEntityProcessor(new EntityProcessorStandard()
                .withRangeMod(2.0F)
                .withDamageMod(new CustomDamageHandlerAmat(50.0F)));
        this.setPlayerProcessor(new PlayerProcessorStandard());
        this.setSFX(new ExplosionEffectAmat());
    }
}
