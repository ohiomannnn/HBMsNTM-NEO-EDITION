package com.hbm.blocks;

import net.minecraft.util.StringRepresentable;

public enum DummyBlockType implements StringRepresentable {
    DUMMY("dummy"),
    EXTRA("extra"),
    CORE("core");

    private final String name;

    DummyBlockType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}