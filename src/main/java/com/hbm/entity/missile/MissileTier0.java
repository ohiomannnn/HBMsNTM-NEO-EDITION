package com.hbm.entity.missile;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.effect.BlackHole;
import com.hbm.entity.logic.EMP;
import com.hbm.entity.logic.NukeExplosionMK3;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.explosion.ExplosionChaos;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.items.ModItems;
import com.hbm.items.special.PolaroidItem;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.particle.helper.CloudCreator;
import com.hbm.particle.helper.ExplosionCreator;
import com.hbm.util.RayTraceResult;
import com.hbm.world.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class MissileTier0 extends MissileBaseNT {

    public MissileTier0(EntityType<? extends MissileTier0> entityType, Level level) { super(entityType, level); }

    @Override
    public List<ItemStack> getDebris() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(ModItems.NOTHING.get(), 4));
        list.add(new ItemStack(ModItems.NOTHING.get(), 4));
        list.add(new ItemStack(ModItems.NOTHING.get(), 2));
        list.add(new ItemStack(ModItems.NOTHING.get(), 1));
        return list;
    }

    @Override
    protected float getContrailScale() {
        return 0.5F;
    }

    public static class MissileMicro extends MissileTier0 {
        public MissileMicro(EntityType<? extends MissileMicro> entityType, Level level) { super(entityType, level); }
        @Override
        public void onMissileImpact(RayTraceResult mop) {

            WorldUtil.loadAndSpawnEntityInWorld(NukeExplosionMK5.statFacNoSpawn(level, MainConfig.COMMON.FATMAN_RADIUS.get(), this.position.x, this.position.y, this.position.z));

            // this has to be the single worst solution ever
            level.playSound(null, this.position.x, this.position.y, this.position.z, ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 25.0F, 1F);
            CompoundTag tag = new CompoundTag();
            tag.putString("type", "muke");
            tag.putBoolean("balefire", PolaroidItem.polaroidID == 11 || level.random.nextInt(100) == 0);
            if (level instanceof ServerLevel serverLevel) {
                PacketDistributor.sendToPlayersNear(serverLevel, null, this.position.x, this.position.y, this.position.z, 250, new AuxParticle(tag, this.position.x, this.position.y, this.position.z));
            }

        }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_MICRO.get()); }
    }

    public static class MissileSchrabidium extends MissileTier0 {
        public MissileSchrabidium(EntityType<? extends MissileSchrabidium> entityType, Level level) { super(entityType, level); }
        @Override
        public void onMissileImpact(RayTraceResult mop) {
            NukeExplosionMK3 ex = NukeExplosionMK3.statFacFleija(level, this.position.x, this.position.y, this.position.z, MainConfig.COMMON.FLEIJA_RADIUS.get());
            if (!ex.isRemoved()) {
                WorldUtil.loadAndSpawnEntityInWorld(ex);
                CloudCreator.composeEffect(level,this.position.x, this.position.y, this.position.z, CloudCreator.CloudType.FLEIJA);
            }
        }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_SCHRABIDIUM.get()); }
    }

    public static class MissileBHole extends MissileTier0 {
        public MissileBHole(EntityType<? extends MissileBHole> entityType, Level level) { super(entityType, level); }
        @Override
        public void onMissileImpact(RayTraceResult mop) {
            ExplosionVNT.createExplosion(level, this, this.position.x, this.position.y, this.position.z, 5.0F, true);
            BlackHole bl = new BlackHole(ModEntityTypes.BLACK_HOLE.get(), this.level);
            bl.setPos(this.position);
            level.addFreshEntity(bl);
        }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_BHOLE.get()); }
    }

    public static class MissileTaint extends MissileTier0 {
        public MissileTaint(EntityType<? extends MissileTaint> entityType, Level level) { super(entityType, level); }
        @Override
        public void onMissileImpact(RayTraceResult mop) {
            ExplosionVNT.createExplosion(level, this, this.position.x, this.position.y, this.position.z, 5.0F, true);
            for (int i = 0; i < 100; i++) {
                int a = random.nextInt(11) + mop.getBlockPos().getX() - 5;
                int b = random.nextInt(11) + mop.getBlockPos().getY() - 5;
                int c = random.nextInt(11) + mop.getBlockPos().getZ() - 5;
                BlockPos randPos = new BlockPos(a, b, c);
                BlockState state = level.getBlockState(randPos);
                if (state.isSolidRender(level, randPos) && !state.isAir()) {
                    level.setBlock(randPos, ModBlocks.TAINT.get().defaultBlockState(), 3);
                }
            }
        }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_TAINT.get()); }
    }

    public static class MissileEMP extends MissileTier0 {
        public MissileEMP(EntityType<? extends MissileEMP> entityType, Level level) { super(entityType, level); }
        @Override
        public void onMissileImpact(RayTraceResult mop) {
            // place holde
            EMP emp = new EMP(ModEntityTypes.EMP.get(), this.level);
            emp.setPos(this.position);
            level.addFreshEntity(emp);
        }
        @Override public void cluster() { this.onMissileImpact(null); }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_EMP.get()); }
    }
}
