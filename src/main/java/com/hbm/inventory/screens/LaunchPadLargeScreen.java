package com.hbm.inventory.screens;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.bomb.LaunchPadBaseBlockEntity;
import com.hbm.inventory.menus.LaunchPadLargeMenu;
import com.hbm.items.weapon.MissileItem;
import com.hbm.render.item.ItemRenderMissileGeneric;
import com.hbm.render.item.RenderStarter;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public class LaunchPadLargeScreen extends InfoScreen<LaunchPadLargeMenu> {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/weapon/gui_launch_pad_large.png");

    public LaunchPadBaseBlockEntity be;

    public LaunchPadLargeScreen(LaunchPadLargeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 236;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 107, this.topPos + 88 - 52, 16, 52, be.power, be.maxPower);
        be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 125, this.topPos + 88 - 52, 16, 52);
        be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 143, this.topPos + 88 - 52, 16, 52);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int partialTicks) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        int fuel = be.getFuelState();
        int oxidizer = be.getOxidizerState();

        if (fuel == 1) guiGraphics.blit(TEXTURE, this.leftPos + 130, this.topPos + 23, 192, 0, 6, 8);
        if (fuel == -1) guiGraphics.blit(TEXTURE, this.leftPos + 130, this.topPos + 23, 198, 0, 6, 8);
        if (oxidizer == 1) guiGraphics.blit(TEXTURE, this.leftPos + 148, this.topPos + 23, 192, 0, 6, 8);
        if (oxidizer == -1) guiGraphics.blit(TEXTURE, this.leftPos + 148, this.topPos + 23, 198, 0, 6, 8);
        if (be.isMissileValid()) guiGraphics.blit(TEXTURE, this.leftPos + 112, this.topPos + 23, be.power >= 75_000 ? 192 : 198, 0, 6, 8);

        int power = (int) (be.power * 52 / be.maxPower);
        guiGraphics.blit(TEXTURE, this.leftPos + 107, this.topPos + 88 - power, 176, 52 - power, 16, power);
        be.tanks[0].renderTank(this.leftPos + 125, this.topPos + 88, 0, 16, 52);
        be.tanks[1].renderTank(this.leftPos + 143, this.topPos + 88, 0, 16, 52);

        if (!be.slots.get(0).isEmpty()) {
            Consumer<RenderStarter> render = ItemRenderMissileGeneric.renderers.get(be.slots.get(0).getItem());
            if (render != null) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(this.leftPos + 70F, this.topPos + 120F, 100F);

                float scale = 1F;

                if (be.slots.get(0).getItem() instanceof MissileItem missileItem) {
                    switch (missileItem.formFactor) {
                        case ABM: scale = 1.45F; break;
                        case MICRO: scale = 2.5F; break;
                        case V2: scale = 1.75F; break;
                        case STRONG: scale = 1.375F; break;
                        case HUGE: scale = 0.925F; break;
                        case ATLAS: scale = 0.875F; break;
                        case OTHER: break;
                    }
                   // if(missile == ModItems.missile_stealth) scale = 1.125D;
                }

                guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(90F));
                guiGraphics.pose().scale(scale, scale, scale);
                guiGraphics.pose().scale(-8F, -8F, -8F);

                guiGraphics.pose().pushPose();
                Lighting.setupForFlatItems();
                guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(75F));
                guiGraphics.pose().popPose();

                render.accept(new RenderStarter(guiGraphics.bufferSource(), guiGraphics.pose(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY));
                Lighting.setupFor3DItems();

                guiGraphics.pose().popPose();
            }
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.leftPos + 34F, this.topPos + 107F, 0F);
        String text = "";
        int color = 0xffffff;
        if (be.state == LaunchPadBaseBlockEntity.STATE_MISSING) {
            guiGraphics.pose().scale(0.5F, 0.5F, 1F);
            text = "notReady";
            color = 0xff0000;
        }
        if( be.state == LaunchPadBaseBlockEntity.STATE_LOADING) {
            guiGraphics.pose().scale(0.6F, 0.6F, 1F);
            text = "loading";
            color = 0xff8000;
        }
        if (be.state == LaunchPadBaseBlockEntity.STATE_READY) {
            guiGraphics.pose().scale(0.8F, 0.8F, 1F);
            text = "ready";
            color = 0x00ff000;
        }
        guiGraphics.drawString(this.font, Component.translatable("gui.launchPad." + text).withColor(color), - this.font.width(text) / 2, - this.font.lineHeight / 2, color, false);
        guiGraphics.pose().popPose();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - font.width(this.title) / 2, 4, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }
}
