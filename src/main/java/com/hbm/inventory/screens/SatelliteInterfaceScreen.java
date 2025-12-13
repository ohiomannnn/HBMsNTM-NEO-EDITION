package com.hbm.inventory.screens;

import com.hbm.HBMsNTM;
import com.hbm.items.ISatChip;
import com.hbm.items.tools.SatelliteInterfaceItem;
import com.hbm.network.toserver.SatelliteLaser;
import com.hbm.saveddata.satellite.Satellite;
import com.hbm.saveddata.satellite.Satellite.Interfaces;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.UUID;

public class SatelliteInterfaceScreen extends Screen {

    protected static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/satellites/gui_sat_interface.png");
    protected int xSize = 216;
    protected int ySize = 216;
    protected int guiLeft;
    protected int guiTop;
    private Player player;
    private int x;
    private int z;

    private NativeImage mapImage;
    private DynamicTexture mapTexture;
    private ResourceLocation mapTextureLocation;

    @Override
    protected void init() {

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        x = (int) player.getX();
        z = (int) player.getZ();

        this.mapImage = new NativeImage(200, 200, false);
        this.mapTexture = new DynamicTexture(mapImage);
        this.mapTextureLocation = this.minecraft.getTextureManager().register("sat_map_" + UUID.randomUUID(), mapTexture);

        this.scanPos = 0;
        this.mapImage.fillRect(0, 0, 200, 200, 0);
        this.mapTexture.upload();
    }

    @Override
    public void onClose() {
        super.onClose();

        if (this.mapTexture != null) {
            this.mapTexture.close();
        }
        if (this.mapImage != null) {
            this.mapImage.close();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (SatelliteInterfaceItem.currentSat != null && SatelliteInterfaceItem.currentSat.ifaceAcs.contains(Satellite.InterfaceActions.CAN_CLICK)) {
            if (mouseX >= this.guiLeft + 8 && mouseX < this.guiLeft + 208 && mouseY >= this.guiTop + 8 && mouseY < this.guiTop + 208 && player != null) {
                int x = (int) (this.x - guiLeft + mouseX - 8 - 100);
                int z = (int) (this.z - guiTop + mouseY - 8 - 100);

                PacketDistributor.sendToServer(new SatelliteLaser(x, z, ISatChip.getFreqS(player.getMainHandItem())));
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public SatelliteInterfaceScreen(Player player) {
        super(Component.empty());
        this.player = player;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
    }

    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderTransparentBackground(guiGraphics);
        this.renderBg(guiGraphics, mouseX, mouseY);
    }

    protected void renderBg(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.renderCoords(guiGraphics, mouseX, mouseY);
        this.renderOther(guiGraphics);
    }

    protected void renderCoords(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (SatelliteInterfaceItem.currentSat != null && SatelliteInterfaceItem.currentSat.ifaceAcs.contains(Satellite.InterfaceActions.SHOW_COORDS)) {
            if (mouseX >= this.guiLeft + 8 && mouseX < this.guiLeft + 208 && mouseY >= this.guiTop + 8 && mouseY < this.guiTop + 208 && player != null) {
                int x = this.x - guiLeft + mouseX - 8 - 100;
                int z = this.z - guiTop + mouseY - 8 - 100;

                guiGraphics.renderComponentTooltip(this.minecraft.font, List.of(Component.literal(x + " / " + z)), mouseX, mouseY);
            }
        }
    }

    protected void renderOther(GuiGraphics guiGraphics) {
        guiGraphics.blit(TEXTURE, guiLeft, guiTop, 0, 0, xSize, ySize);

        if (SatelliteInterfaceItem.currentSat == null) {
            renderNotConnected(guiGraphics);
        } else {
            if (SatelliteInterfaceItem.currentSat.satIface != Interfaces.SAT_PANEL) {
                renderNoService(guiGraphics);
                return;
            }

            if (SatelliteInterfaceItem.currentSat.ifaceAcs.contains(Satellite.InterfaceActions.HAS_MAP)) {
                drawMap(guiGraphics);
            }

            if (SatelliteInterfaceItem.currentSat.ifaceAcs.contains(Satellite.InterfaceActions.HAS_RADAR)) {
                renderRadar(guiGraphics);
            }
        }
    }

    private int scanPos = 0;
    private long lastMilli = 0;

    private void progresScan() {

        if (lastMilli + 25 < System.currentTimeMillis()) {
            lastMilli = System.currentTimeMillis();
            scanPos++;
        }

        if (scanPos >= 200) {
            scanPos -= 200;
        }
    }

    private void drawMap(GuiGraphics guiGraphics) {
        Level level = player.level();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int i = -100; i < 100; i++) {
            int worldX = this.x + i;
            int worldZ = this.z + scanPos - 100;
            int worldY = level.getHeight(Heightmap.Types.WORLD_SURFACE, worldX, worldZ) - 1;

            pos.set(worldX, worldY, worldZ);

            int rgb = level.getBlockState(pos).getMapColor(level, pos).calculateRGBColor(MapColor.Brightness.NORMAL);

            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;

            mapImage.setPixelRGBA(i + 100, scanPos, FastColor.ARGB32.color(255, r, g, b));
        }
        mapTexture.upload();

        guiGraphics.blit(this.mapTextureLocation, guiLeft + 8, guiTop + 8, 0, 0, 200, 200, 200, 200);
        progresScan();
    }

    private void renderRadar(GuiGraphics guiGraphics) {
        List<Entity> entities = player.level().getEntities(player, new AABB(player.getX() - 100, 0, player.getZ() - 100, player.getX() + 100, 5000, player.getZ() + 100));

        if (!entities.isEmpty()) {
            for (Entity entity : entities) {
                if (entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() >= 0.5D) {
                    int x = (int)((entity.getX() - this.x) / ((double)100 * 2 + 1) * (200D - 8D)) - 4;
                    int z = (int)((entity.getZ() - this.z) / ((double)100 * 2 + 1) * (200D - 8D)) - 4 - 9;

                    int t = 5;

                    if (entity instanceof Mob) {
                        t = 6;
                    }

                    if (entity instanceof Player) {
                        t = 7;
                    }

                    guiGraphics.blit(TEXTURE, guiLeft + 108 + x, guiTop + 117 + z, 216, 8 * t, 8, 8);
                }
            }
        }
    }

    private void renderNoService(GuiGraphics guiGraphics) {
        guiGraphics.blit(TEXTURE, (this.width - 77) / 2, (this.height - 12) / 2, 0, 228, 77, 12);
    }

    private void renderNotConnected(GuiGraphics guiGraphics) {
        guiGraphics.blit(TEXTURE, (this.width - 121) / 2, (this.height - 12) / 2, 0, 216, 121, 12);
    }
}
