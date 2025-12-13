package com.hbm.saveddata.satellite;

public class SatelliteRadar extends Satellite {
    public SatelliteRadar() {
        this.ifaceAcs.add(InterfaceActions.HAS_MAP);
        this.ifaceAcs.add(InterfaceActions.HAS_RADAR);
        this.ifaceAcs.add(InterfaceActions.SHOW_COORDS);
        this.satIface = Interfaces.SAT_PANEL;
    }
}
