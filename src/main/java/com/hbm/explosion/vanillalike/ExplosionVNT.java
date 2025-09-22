package com.hbm.explosion.vanillalike;

import com.hbm.explosion.vanillalike.interfaces.*;
import com.hbm.explosion.vanillalike.standard.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//TODO: make ts with packets

public class ExplosionVNT {

    private IBlockAllocator blockAllocator;
    private IEntityProcessor entityProcessor;
    private IBlockProcessor blockProcessor;
    private IPlayerProcessor playerProcessor;
    private IExplosionSFX[] sfx;

    public Level level;
    public double posX;
    public double posY;
    public double posZ;
    public float size;
    public Entity exploder;

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
    }

    public void explode() {
        if (this.level.isClientSide) {
            if (sfx != null) {
                for (IExplosionSFX fx : sfx) {
                    fx.doEffect(this, level, posX, posY, posZ, size);
                }
            }
            return;
        }

        boolean processBlocks = blockAllocator != null && blockProcessor != null;
        boolean processEntities = entityProcessor != null && playerProcessor != null;

        HashSet<BlockPos> affectedBlocks = null;
        Map<Player, Vec3> affectedPlayers = null;

        if (processBlocks) {
            affectedBlocks = blockAllocator.allocate(this, level, posX, posY, posZ, size);
        }
        if (processEntities) {
            affectedPlayers = entityProcessor.processEntities(this, level, posX, posY, posZ, size);
        }

        if (processBlocks) {
            blockProcessor.processBlocks(this, level, posX, posY, posZ, affectedBlocks);
        }
        if (processEntities) {
            playerProcessor.processPlayers(this, level, posX, posY, posZ, affectedPlayers);
        }

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
        this.setBlockProcessor(new BlockProcessorStandard().setNoDrop());
        this.setEntityProcessor(new EntityProcessorStandard()
                .withRangeMod(2.0F)
                .withDamageHandler(new CustomDamageHandlerAmat(50.0F)));
        this.setPlayerProcessor(new PlayerProcessorStandard());
        this.setSFX(new ExplosionEffectAmat());
    }
}
