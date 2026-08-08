package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.util.ChatBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.List;

public class FT_VentRadiation extends FluidTrait {

    float radPerMB = 0;

    public FT_VentRadiation() { }

    public FT_VentRadiation(float rad) {
        this.radPerMB = rad;
    }

    public float getRadPerMB() {
        return this.radPerMB;
    }

    @Override
    public void onFluidRelease(Level level, BlockPos pos, FluidTank tank, int overflowAmount, FluidReleaseType type) {
        ChunkRadiationManager.proxy.incrementRad(level, pos, overflowAmount * radPerMB);
    }

    @Override
    public void addInfo(List<Component> info) {
        info.add(ChatBuilder.start("[").nextTranslation("fluids.trait.radioactive").next("]").colorAll(ChatFormatting.YELLOW).flush());
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("radiation").value(radPerMB);
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        this.radPerMB = obj.get("radiation").getAsFloat();
    }
}
