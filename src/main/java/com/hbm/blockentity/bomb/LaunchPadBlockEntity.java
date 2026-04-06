package com.hbm.blockentity.bomb;

import com.hbm.main.NuclearTechModClient;
import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.entity.missile.MissileBaseNT;
import com.hbm.lib.Library;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class LaunchPadBlockEntity extends LaunchPadBaseBlockEntity {

    public LaunchPadBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.LAUNCH_PAD.get(), pos, state);
    }

    @Override public boolean isReadyForLaunch() { return delay <= 0; }
    @Override public double getLaunchOffset() { return 1D; }

    public int delay = 0;

    @Override
    public void updateEntity() {
        if (level == null) return;

        if (!level.isClientSide) {
            if (this.delay > 0) delay--;

            if (!this.isMissileValid() || !this.hasFuel()) {
                this.delay = 100;
            }

            if (!this.hasFuel() || !this.isMissileValid()) {
                this.state = STATE_MISSING;
            } else {
                if(this.delay > 0) {
                    this.state = STATE_LOADING;
                } else {
                    this.state = STATE_READY;
                }
            }
        } else {

            int x = this.getBlockPos().getX();
            int y = this.getBlockPos().getY();
            int z = this.getBlockPos().getZ();
            List<MissileBaseNT> entities = level.getEntitiesOfClass(MissileBaseNT.class, new AABB(x - 0.5, y, z - 0.5, x + 1.5, y + 10, z + 1.5));

            if (!entities.isEmpty()) {
                for (int i = 0; i < 15; i++) {

                    Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
                    if (level.random.nextBoolean()) dir = dir.getOpposite();
                    if (level.random.nextBoolean()) dir = dir.getClockWise();
                    float moX = (float) (level.random.nextGaussian() * 0.15F + 0.75) * dir.getStepX();
                    float moZ = (float) (level.random.nextGaussian() * 0.15F + 0.75) * dir.getStepZ();

                    CompoundTag tag = new CompoundTag();
                    tag.putDouble("posX", x + 0.5);
                    tag.putDouble("posY", y + 0.25);
                    tag.putDouble("posZ", z + 0.5);
                    tag.putString("type", "launchSmoke");
                    tag.putDouble("moX", moX);
                    tag.putDouble("moY", 0);
                    tag.putDouble("moZ", moZ);
                    NuclearTechModClient.effectNT(tag);
                }
            }
        }

        super.updateEntity();
    }

    @Override
    public void finalizeLaunch(Entity missile) {
        super.finalizeLaunch(missile);
        this.delay = 100;
    }

    @Override
    public DirPos[] getConPos() {
        int x = this.getBlockPos().getX();
        int y = this.getBlockPos().getY();
        int z = this.getBlockPos().getZ();
        return new DirPos[] {
                new DirPos(x + 2, y, z - 1, Library.POS_X),
                new DirPos(x + 2, y, z + 1, Library.POS_X),
                new DirPos(x - 2, y, z - 1, Library.NEG_X),
                new DirPos(x - 2, y, z + 1, Library.NEG_X),
                new DirPos(x - 1, y, z + 2, Library.POS_Z),
                new DirPos(x + 1, y, z + 2, Library.POS_Z),
                new DirPos(x - 1, y, z - 2, Library.NEG_Z),
                new DirPos(x + 1, y, z - 2, Library.NEG_Z)
        };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.delay = tag.getInt("Delay");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("delay", delay);
    }
}
