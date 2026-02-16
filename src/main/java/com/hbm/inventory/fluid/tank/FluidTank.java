package com.hbm.inventory.fluid.tank;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.util.BobMathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FluidTank {

    public static final FluidTank[] EMPTY_ARRAY = new FluidTank[0];

    public static final List<FluidLoadingHandler> loadingHandlers = new ArrayList<>();
    public static final Set<Item> noDualUnload = new HashSet<>();

    static {
        loadingHandlers.add(new FluidLoaderStandard());
        loadingHandlers.add(new FluidLoaderFillableItem());
        loadingHandlers.add(new FluidLoaderInfinite());
    }

    protected FluidType type;
    protected int fluid;
    protected int maxFluid;
    protected int pressure = 0;

    public FluidTank(FluidType type, int maxFluid) {
        this.type = type;
        this.maxFluid = maxFluid;
    }

    public FluidTank withPressure(int pressure) {
        if (this.pressure != pressure) this.setFill(0);
        this.pressure = pressure;
        return this;
    }

    public void setFill(int i) { fluid = i; }

    public void setTankType(FluidType type) {
        if (type == null) type = Fluids.NONE;
        if (this.type == type) return;

        this.type = type;
        this.setFill(0);
    }

    public void resetTank() {
        this.type = Fluids.NONE;
        this.fluid = 0;
        this.pressure = 0;
    }

    /** Changes type and pressure based on a fluid stack, useful for changing tank types based on recipes */
    public FluidTank conform(FluidStack stack) {
        this.setTankType(stack.type);
        this.withPressure(stack.pressure);
        return this;
    }

    public FluidType getTankType() { return type; }
    public int getFill() { return fluid; }
    public int getMaxFill() { return maxFluid; }
    public int getPressure() { return pressure; }

    public int changeTankSize(int size) {
        maxFluid = size;

        if (fluid > maxFluid) {
            int dif = fluid - maxFluid;
            fluid = maxFluid;
            return dif;
        }
        return 0;
    }

    // Fills tank from canisters
    public boolean loadTank(Level level, int in, int out, NonNullList<ItemStack> slots) {
        if (slots.get(in).isEmpty()) return false;

        boolean isInfiniteBarrel = slots.get(in).getItem() == ModItems.FLUID_BARREL_INFINITE.get();
        if (!isInfiniteBarrel && pressure != 0) return false;

        int prev = this.getFill();

        for (FluidLoadingHandler handler : loadingHandlers) {
            if (handler.emptyItem(level, slots, in, out, this)) {
                break;
            }
        }

        return this.getFill() > prev;
    }

    // Fills canisters from tank
    public boolean unloadTank(Level level, int in, int out, NonNullList<ItemStack> slots) {
        if (slots.get(in).isEmpty()) return false;

        int prev = this.getFill();

        for (FluidLoadingHandler handler : loadingHandlers) {
            if (handler.fillItem(level, slots, in, out, this)) {
                break;
            }
        }

        return this.getFill() < prev;
    }

    public boolean setType(int in, NonNullList<ItemStack> slots) {
        return setType(in, in, slots);
    }

    /**
     * Changes the tank type and returns true if successful
     */
    public boolean setType(int in, int out, NonNullList<ItemStack> slots) {

        if (slots.get(in).getItem() instanceof IItemFluidIdentifier id) {
            if (in == out) {
                FluidType newType = id.getType(null, BlockPos.ZERO, slots.get(in));

                if (type != newType) {
                    type = newType;
                    fluid = 0;
                    return true;
                }

            } else if (slots.get(out).isEmpty()) {
                FluidType newType = id.getType(null, BlockPos.ZERO, slots.get(in));
                if (type != newType) {
                    type = newType;
                    slots.set(out, slots.get(in).copy());
                    slots.set(in, ItemStack.EMPTY);
                    fluid = 0;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Renders the fluid texture into a GUI, with the height based on the fill pressed
     * @param x the tank's left side
     * @param y the tank's bottom side (convention from the old system, changing it now would be a pain in the ass)
     * @param z the GUI's zLevel
     */
    @OnlyIn(Dist.CLIENT)
    public void renderTank(int x, int y, float z, int width, int height) {
        renderTank(x, y, z, width, height, 0);
    }

    @OnlyIn(Dist.CLIENT)
    public void renderTank(int x, int y, float z, int width, int height, int orientation) {

        RenderSystem.enableBlend();

        int color = type.getTint();
        float r = (float) (((color & 0xff0000) >> 16) / 255D);
        float g = (float) (((color & 0x00ff00) >> 8) / 255D);
        float b = (float) (((color & 0x0000ff) >> 0) / 255D);

        y -= height;

        int i = (fluid * height) / maxFluid;

        float minX = x;
        float maxX = x;
        float minY = y;
        float maxY = y;

        float minV = 1F - i / 16F;
        float maxV = 1F;
        float minU = 0F;
        float maxU = width / 16F;

        if (orientation == 0) {
            maxX += width;
            minY += height - i;
            maxY += height;
        }

        if (orientation == 1) {
            i = (fluid * width) / maxFluid;
            maxX += i;
            maxY += height;

            minV = 0F;
            maxV = height / 16F;
            minU = 1F;
            maxU = 1F - i / 16F;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, type.getTexture());

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        buf.addVertex(minX, maxY, z).setUv(minU, maxV).setColor(r, g, b, 1.0F);
        buf.addVertex(maxX, maxY, z).setUv(maxU, maxV).setColor(r, g, b, 1.0F);
        buf.addVertex(maxX, minY, z).setUv(maxU, minV).setColor(r, g, b, 1.0F);
        buf.addVertex(minX, minY, z).setUv(minU, minV).setColor(r, g, b, 1.0F);

        BufferUploader.drawWithShader(buf.buildOrThrow());

        RenderSystem.disableBlend();
    }

    @OnlyIn(Dist.CLIENT)
    public void renderTankTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int width, int height) {
        if (x <= mouseX && x + width > mouseX && y < mouseY && y + height >= mouseY) {

            List<Component> list = new ArrayList<>();
            list.add(this.type.getName());
            list.add(Component.translatable("fluid.info.mb_out", this.fluid, this.maxFluid));

            if (this.pressure != 0) {
                list.add(Component.translatable("fluid.info.pressure", this.pressure).withStyle(ChatFormatting.RED));
                list.add(Component.translatable("fluid.info.pressurized").withStyle(BobMathUtil.getBlink() ? ChatFormatting.RED : ChatFormatting.DARK_RED));
            }

            type.addInfo(list);
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, list, mouseX, mouseY);
        }
    }

    // Called by BE to save fillstate
    public void writeToNBT(CompoundTag tag, String s) {
        tag.putInt(s, fluid);
        tag.putInt(s + "_max", maxFluid);
        tag.putInt(s + "_type", type.getID());
        tag.putShort(s + "_p", (short) pressure);
    }

    // Called by BE to load fillstate
    public void readFromNBT(CompoundTag tag, String s) {
        fluid = tag.getInt(s);
        int max = tag.getInt(s + "_max");
        if (max > 0) maxFluid = max;

        fluid = Mth.clamp(fluid, 0, max);

        type = Fluids.fromNameCompat(tag.getString(s + "_type")); // compat
        if (type == Fluids.NONE) type = Fluids.fromID(tag.getInt(s + "_type"));

        this.pressure = tag.getShort(s + "_p");
    }

    public void serialize(ByteBuf buf) {
        buf.writeInt(fluid);
        buf.writeInt(maxFluid);
        buf.writeInt(type.getID());
        buf.writeShort((short) pressure);
    }

    public void deserialize(ByteBuf buf) {
        fluid = buf.readInt();
        maxFluid = buf.readInt();
        type = Fluids.fromID(buf.readInt());
        pressure = buf.readShort();
    }
}
