package api.hbm.energymk2;

import api.hbm.blockentity.ILoadedTile;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

/** DO NOT USE DIRECTLY! This is simply the common ancestor to providers and receivers, because all this behavior has to be excluded from conductors! */
public interface IEnergyHandlerMK2 extends IEnergyConnectorMK2, ILoadedTile {

    long getPower();
    void setPower(long power);
    long getMaxPower();

    boolean particleDebug = false;

    default Vec3 getDebugParticlePosMK2() {
        BlockEntity be = (BlockEntity) this;
        return new Vec3(be.getBlockPos().getX() + 0.5, be.getBlockPos().getY() + 1, be.getBlockPos().getZ() + 0.5);
    }

    // TODO something???
//    default void provideInfoForECMK2(CompoundTag tag) {
//        tag.putLong(CompatEnergyControl.L_ENERGY_HE, this.getPower());
//        tag.putLong(CompatEnergyControl.L_CAPACITY_HE, this.getMaxPower());
//    }
}
