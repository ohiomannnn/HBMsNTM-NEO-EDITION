package com.hbm.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class EnumTypes {

    public static final EnumProperty<PipeTypes> PIPE_TYPES = EnumProperty.create("pipe_type", PipeTypes.class);

    public enum PipeTypes implements StringRepresentable {
        NEO("neo"),
        SILVER("silver"),
        COLORED("colored");

        private final String name;
        PipeTypes(String name) { this.name = name; }
        @Override public String getSerializedName() { return name; }
    }
}
