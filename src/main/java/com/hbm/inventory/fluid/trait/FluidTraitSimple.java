package com.hbm.inventory.fluid.trait;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public class FluidTraitSimple {

    public static class FT_Gaseous extends FluidTrait {
        @Override public void addInfoHidden(List<Component> info) {
            info.add(Component.translatable("fluid.trait.gaseous").withStyle(ChatFormatting.BLUE));
        }
    }

    /** gaseous at room temperature, for cryogenic hydrogen for example */
    public static class FT_Gaseous_ART extends FluidTrait {
        @Override public void addInfoHidden(List<Component> info) {
            info.add(Component.translatable("fluid.trait.gaseous_atr").withStyle(ChatFormatting.BLUE));
        }
    }

    public static class FT_Liquid extends FluidTrait {
        @Override public void addInfoHidden(List<Component> info) {
            info.add(Component.translatable("fluid.trait.liquid").withStyle(ChatFormatting.BLUE));
        }
    }

    /** to viscous - to be sprayed/turned into a mist */
    public static class FT_Viscous extends FluidTrait {
        @Override public void addInfoHidden(List<Component> info) {
            info.add(Component.translatable("fluid.trait.viscous").withStyle(ChatFormatting.BLUE));
        }
    }

    public static class FT_Plasma extends FluidTrait {
        @Override public void addInfoHidden(List<Component> info) {
            info.add(Component.translatable("fluid.trait.plasma").withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }

    public static class FT_Amat extends FluidTrait {
        @Override public void addInfo(List<Component> info) {
            info.add(Component.translatable("fluid.trait.amat").withStyle(ChatFormatting.DARK_RED));
        }
    }

    public static class FT_LeadContainer extends FluidTrait {
        @Override public void addInfo(List<Component> info) {
            info.add(Component.translatable("fluid.trait.lead_container").withStyle(ChatFormatting.DARK_RED));
        }
    }

    public static class FT_Delicious extends FluidTrait {
        @Override public void addInfoHidden(List<Component> info) {
            info.add(Component.translatable("fluid.trait.delicious").withStyle(ChatFormatting.DARK_GREEN));
        }
    }

    public static class FT_Unsiphonable extends FluidTrait {
        @Override public void addInfoHidden(List<Component> info) {
            info.add(Component.translatable("fluid.trait.unsiphonable").withStyle(ChatFormatting.BLUE));
        }
    }

    public static class FT_NoID extends FluidTrait { }
    public static class FT_NoContainer extends FluidTrait { }
}
