package com.hbm.saveddata.satellite;

import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.logic.DeathBlast;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class SatelliteLaser extends Satellite {

    public long lastOp;

    public SatelliteLaser() {
        this.ifaceAcs.add(InterfaceActions.HAS_MAP);
        this.ifaceAcs.add(InterfaceActions.SHOW_COORDS);
        this.ifaceAcs.add(InterfaceActions.CAN_CLICK);
        this.satIface = Interfaces.SAT_PANEL;
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        nbt.putLong("lastOp", lastOp);
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        lastOp = nbt.getLong("lastOp");
    }

    @Override
    public void onClick(Level level, int x, int z) {
        if (lastOp + 10000 < System.currentTimeMillis()) {
            lastOp = System.currentTimeMillis();

            int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

            DeathBlast blast = new DeathBlast(ModEntityTypes.DEATH_BLAST.get(), level);
            blast.setPos(x, y, z);

            level.addFreshEntity(blast);
        }
    }
}
