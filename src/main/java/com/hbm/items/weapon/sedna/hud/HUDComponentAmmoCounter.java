package com.hbm.items.weapon.sedna.hud;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.items.weapon.sedna.mags.IMagazine;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public class HUDComponentAmmoCounter implements IHUDComponent {

    protected int receiver;
    protected boolean mirrored;
    protected boolean noCounter;

    public HUDComponentAmmoCounter(int receiver) {
        this.receiver = receiver;
    }

    public HUDComponentAmmoCounter mirror() {
        this.mirrored = true;
        return this;
    }

    public HUDComponentAmmoCounter noCounter() {
        this.noCounter = true;
        return this;
    }

    @Override
    public int getComponentHeight(Player player, ItemStack stack) {
        return 17;
    }

    @Override
    public void renderHUDComponent(RenderGuiLayerEvent.Pre event, Player player, ItemStack stack, int bottomOffset, int gunIndex) {

        if(!event.getName().equals(VanillaGuiLayers.HOTBAR)) return;
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Window window = mc.getWindow();

        int pX = window.getGuiScaledWidth() / 2 + (mirrored ? -(62 + 36 + 52) : (62 + 36)) + (noCounter ? 14 : 0);
        int pZ = window.getGuiScaledHeight() - bottomOffset - 18;
        GunBaseNTItem gun = (GunBaseNTItem) stack.getItem();
        IMagazine mag = gun.getConfig(stack, gunIndex).getReceivers(stack)[this.receiver].getMagazine(stack);

        if(!noCounter) guiGraphics.drawString(mc.font, mag.reportAmmoStateForHUD(stack, player), pX + 17, pZ + 6, 0xFFFFFF);

        guiGraphics.renderItem(mag.getIconForHUD(stack, player), pX, pZ);
    }
}
