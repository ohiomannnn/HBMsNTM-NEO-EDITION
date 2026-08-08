package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.util.BobMathUtil;
import com.hbm.util.ChatBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.io.IOException;
import java.util.List;

public class FT_Combustible extends FluidTrait {

    protected FuelGrade fuelGrade;
    protected long combustionEnergy;

    public FT_Combustible() { }

    public FT_Combustible(FuelGrade grade, long energy) {
        this.fuelGrade = grade;
        this.combustionEnergy = energy;
    }

    @Override
    public void addInfo(List<Component> info) {
        super.addInfo(info);

        info.add(ChatBuilder.start("[").nextTranslation("fluids.trait.combustible").next("]").colorAll(ChatFormatting.GOLD).flush());

        if(combustionEnergy > 0) {
            info.add(Component.translatable("hbmfluid.trait.provides").withStyle(ChatFormatting.GOLD).append(" ").append(BobMathUtil.getShortNumber(combustionEnergy) + "HE ").withStyle(ChatFormatting.GOLD).append(this.fuelGrade.getLocalizedName()).append(Component.translatable("hbmfluid.trait.perBucket").withStyle(ChatFormatting.GOLD)));
            info.add(Component.translatable("hbmfluid.trait.fuelGrade").append(": ").withStyle(ChatFormatting.GOLD).append(this.fuelGrade.getLocalizedName().withStyle(ChatFormatting.RED)));
        }
    }

    public long getCombustionEnergy() {
        return this.combustionEnergy;
    }

    public FuelGrade getGrade() {
        return this.fuelGrade;
    }

    public enum FuelGrade {
        LOW("low"),			//heating and industrial oil				< star engine, iGen
        MEDIUM("medium"),	//petroil									< diesel generator
        HIGH("high"),		//diesel, gasoline							< HP engine
        AERO("aviation"),	//kerosene and other light aviation fuels	< turbofan
        GAS("gaseous");		//fuel gasses like NG, PG and syngas		< gas turbine

        private final String grade;

        FuelGrade(String grade) {
            this.grade = grade;
        }

        public MutableComponent getLocalizedName() {
            return Component.translatable("hbmfluid.trait.fuel." + this.grade);
        }
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("energy").value(combustionEnergy);
        writer.name("grade").value(fuelGrade.name());
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        this.combustionEnergy = obj.get("energy").getAsLong();
        this.fuelGrade = FuelGrade.valueOf(obj.get("grade").getAsString());
    }
}
