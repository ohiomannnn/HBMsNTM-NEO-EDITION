package com.hbm.uninos.networkproviders;

import api.hbm.energymk2.PowerNetMK2;
import com.hbm.uninos.INetworkProvider;

public class PowerNetProvider implements INetworkProvider<PowerNetMK2> {
    @Override
    public PowerNetMK2 provideNetwork() {
        return new PowerNetMK2();
    }
}
