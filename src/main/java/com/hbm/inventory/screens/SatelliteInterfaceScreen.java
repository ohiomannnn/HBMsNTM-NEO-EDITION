package com.hbm.inventory.screens;

import com.hbm.items.ISatChip;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.SatelliteLaser;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.saveddata.satellite.Satellite;
import com.hbm.saveddata.satellite.Satellite.InterfaceActions;
import com.hbm.saveddata.satellite.Satellite.Interfaces;
import com.hbm.util.InventoryUtil;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor.Brightness;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class SatelliteInterfaceScreen extends Screen {

    protected static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/satellites/gui_sat_interface.png");
    protected int imageWidth = 216;
    protected int imageHeight = 216;
    protected int leftPos;
    protected int topPos;
    private final Player player;
    @Nullable private final Satellite satellite;
    private int x;
    private int z;

    private NativeImage mapImage;
    private DynamicTexture mapTexture;
    private ResourceLocation mapTextureLocation;

    public SatelliteInterfaceScreen(Player player, @Nullable Satellite satellite) {
        super(Component.translatable("container.satellite_interface"));

        this.player = player;
        this.satellite = satellite;
    }

    @Override public boolean isPauseScreen() { return false; }

    @Override
    @SuppressWarnings("DataFlowIssue")
    protected void init() {

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

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

        if(this.mapTexture != null) this.mapTexture.close();
        if(this.mapImage != null) this.mapImage.close();
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.satellite != null && this.satellite.ifaceAcs.contains(InterfaceActions.CAN_CLICK)) {
            if(mouseX >= this.leftPos + 8 && mouseX < this.leftPos + 208 && mouseY >= this.topPos + 8 && mouseY < this.topPos + 208 && player != null) {
                int x = (int) (this.x - this.leftPos + mouseX - 8 - 100);
                int z = (int) (this.z - this.topPos + mouseY - 8 - 100);

                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(NtmSoundEvents.TECH_BLEEP, 1.0F));

                PacketDistributor.sendToServer(new SatelliteLaser(x, z, ISatChip.getFreqS(player.getMainHandItem())));

                List<ItemStack> stacks = InventoryUtil.getItemsFromBothHands(player);
                for(ItemStack stack : stacks) {
                    if(stack.getItem() instanceof ISatChip satChip) {
                        PacketDistributor.sendToServer(new SatelliteLaser(x, z, satChip.getFreq(stack)));
                        break;
                    }
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderTransparentBackground(guiGraphics);

        this.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
        this.renderLabels(guiGraphics, mouseX, mouseY);
    }

    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if(this.satellite != null && this.satellite.ifaceAcs.contains(InterfaceActions.SHOW_COORDS)) {
            if(mouseX >= this.leftPos + 8 && mouseX < this.leftPos + 208 && mouseY >= this.topPos + 8 && mouseY < this.topPos + 208 && player != null) {
                int x = this.x - this.leftPos + mouseX - 8 - 100;
                int z = this.z - this.topPos + mouseY - 8 - 100;

                guiGraphics.renderTooltip(this.font, Component.literal(x + " / " + z), mouseX, mouseY);
            }
        }
    }

    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        if(this.satellite == null) {
            this.renderNotConnected(guiGraphics);
        } else {
            if(this.satellite.satIface != Interfaces.SAT_PANEL) {
                this.renderNoService(guiGraphics);
                return;
            }

            if(this.satellite.ifaceAcs.contains(InterfaceActions.HAS_MAP)) this.renderMap(guiGraphics);
            if(this.satellite.ifaceAcs.contains(InterfaceActions.HAS_RADAR)) this.renderRadar(guiGraphics);
        }
    }

    private int scanPos = 0;
    private long lastMilli = 0;

    private void progresScan() {

        if(lastMilli + 25 < System.currentTimeMillis()) {
            this.lastMilli = System.currentTimeMillis();
            this.scanPos++;
        }

        if(this.scanPos >= 200) this.scanPos -= 200;
    }

    private void renderMap(GuiGraphics guiGraphics) {
        Level level = player.level;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for(int i = -100; i < 100; i++) {
            int worldX = this.x + i;
            int worldZ = this.z + scanPos - 100;
            int worldY = level.getHeight(Heightmap.Types.WORLD_SURFACE, worldX, worldZ) - 1;

            pos.set(worldX, worldY, worldZ);

            int rgb = level.getBlockState(pos).getMapColor(level, pos).calculateRGBColor(Brightness.NORMAL);

            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;

            this.mapImage.setPixelRGBA(i + 100, this.scanPos, FastColor.ARGB32.color(255, r, g, b));
        }
        this.mapTexture.upload();

        // prontMap();
        guiGraphics.blit(this.mapTextureLocation, this.leftPos + 8, this.topPos + 8, 0, 0, 200, 200, 200, 200);

        this.progresScan();
    }

    private void renderRadar(GuiGraphics guiGraphics) {
        List<Entity> entities = player.level.getEntities(player, new AABB(player.getX() - 100, 0, player.getZ() - 100, player.getX() + 100, 5000, player.getZ() + 100));

        if(!entities.isEmpty()) {
            for(Entity entity : entities) {
                if(entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() >= 0.5D) {
                    int x = (int)((entity.getX() - this.x) / ((double)100 * 2 + 1) * (200D - 8D)) - 4;
                    int z = (int)((entity.getZ() - this.z) / ((double)100 * 2 + 1) * (200D - 8D)) - 4 - 9;

                    int t = 5;

                    if(entity instanceof Mob) t = 6;
                    if(entity instanceof Player) t = 7;

                    guiGraphics.blit(TEXTURE, this.leftPos + 108 + x, this.topPos + 117 + z, 216, 8 * t, 8, 8);
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

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        if(keyCode == GLFW.GLFW_KEY_ESCAPE || this.minecraft.options.keyInventory.isActiveAndMatches(key)) {
            this.onClose();
            return true;
        }

        // todo make map reset
        if(key.equals(this.minecraft.options.keyUp.getKey())) {
            this.z -= 50;
            return true;
        }
        if(key.equals(this.minecraft.options.keyDown.getKey())) {
            this.z += 50;
            return true;
        }
        if(key.equals(this.minecraft.options.keyLeft.getKey())) {
            this.x -= 50;
            return true;
        }
        if(key.equals(this.minecraft.options.keyRight.getKey())) {
            this.x += 50;
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
