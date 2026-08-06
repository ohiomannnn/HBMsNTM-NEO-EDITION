package com.hbm.items.weapon.sedna.hud;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.render.util.RenderScreenOverlay;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public class HUDComponentDurabilityBar implements IHUDComponent {

    protected final boolean mirrored;

    public HUDComponentDurabilityBar() {
        this(false);
    }
    public HUDComponentDurabilityBar(boolean mirror) {
        this.mirrored = mirror;
    }

    @Override
    public int getComponentHeight(Player player, ItemStack stack) {
        return 5;
    }

    @Override
    public void renderHUDComponent(RenderGuiLayerEvent.Pre event, Player player, ItemStack stack, int bottomOffset, int gunIndex) {

        if(!event.getName().equals(VanillaGuiLayers.HOTBAR)) return;
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Window window = mc.getWindow();

        int pX = window.getGuiScaledWidth() / 2 + (mirrored ? -(62 + 36 + 52) : (62 + 36));
        int pZ = window.getGuiScaledHeight() - 21;

        GunBaseNTItem gun = (GunBaseNTItem) stack.getItem();
        int dura = (int) (50 * GunBaseNTItem.getWear(stack, gunIndex) / gun.getConfig(stack, gunIndex).getDurability(stack));

        guiGraphics.blit(RenderScreenOverlay.MISC_TEXTURE, pX, pZ + 16, 94, 0, 52, 3);
        guiGraphics.blit(RenderScreenOverlay.MISC_TEXTURE, pX + 1, pZ + 16, 95, 3, 50 - dura, 3);
    }
}
