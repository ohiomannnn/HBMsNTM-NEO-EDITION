package com.hbm.uninos;

public interface INetworkProvider<T extends NodeNet> {
    T provideNetwork();
}
