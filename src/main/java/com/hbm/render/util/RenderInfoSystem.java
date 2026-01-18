package com.hbm.render.util;

import com.hbm.config.MainConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.*;

@EventBusSubscriber(value = Dist.CLIENT)
public class RenderInfoSystem {

    private static int nextID = 1000;
    private static final Map<Integer, InfoEntry> inbox = new HashMap<>();
    private static final Map<Integer, InfoEntry> messages = new HashMap<>();

    public static void clear() {
        inbox.clear();
        messages.clear();
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        messages.putAll(inbox);
        inbox.clear();

        List<Integer> keys = new ArrayList<>(messages.keySet());
        long now = System.currentTimeMillis();

        for (int i = 0; i < keys.size(); i++) {
            Integer key = keys.get(i);
            InfoEntry entry = messages.get(key);

            if (entry != null && entry.start + entry.millis < now) {
                messages.remove(key);
                keys = new ArrayList<>(messages.keySet());
                i--;
            }
        }
    }

    @SubscribeEvent
    public static void onOverlayRender(RenderGuiEvent.Pre event) {

        if (messages.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;
        GuiGraphics graphics = event.getGuiGraphics();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        List<InfoEntry> entries = new ArrayList<>(messages.values());
        Collections.sort(entries);

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);

        int longest = 0;
        for (InfoEntry entry : messages.values()) {
            int length = mc.font.width(entry.component);
            if (length > longest) longest = length;
        }

        int mode = MainConfig.CLIENT.INFO_POSITION.get();
        int pX = mode == 0 ? 15 : mode == 1 ? (width - longest - 15) : mode == 2 ? (width / 2 + 7) : (width / 2 - longest - 6);
        int pZ = mode == 0 ? 15 : mode == 1 ? 15 : (height / 2 + 7);

        pX += MainConfig.CLIENT.INFO_OFFSET_HORIZONTAL.get();
        pZ += MainConfig.CLIENT.INFO_OFFSET_VERTICAL.get();

        int side = pX + 5 + longest;
        int infoHeight = messages.size() * 10 + pZ + 2;
        int z = 0;

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        buf.addVertex(pX - 5, pZ - 5, z).setColor(0.25F, 0.25F, 0.25F, 0.5F);
        buf.addVertex(pX - 5, infoHeight, z).setColor(0.25F, 0.25F, 0.25F, 0.5F);
        buf.addVertex(side, infoHeight, z).setColor(0.25F, 0.25F, 0.25F, 0.5F);
        buf.addVertex(side, pZ - 5, z).setColor(0.25F, 0.25F, 0.25F, 0.5F);

        BufferUploader.drawWithShader(buf.buildOrThrow());

        int off = 0;
        long now = System.currentTimeMillis();

        for (InfoEntry entry : messages.values()) {
            int elapsed = (int) (now - entry.start);
            int alpha = Math.max(Math.min(510 * (entry.millis - elapsed) / entry.millis, 255), 5);

            graphics.drawString(mc.font, entry.component, pX, pZ + off, 0xFFFFFF | (alpha << 24));
            off += 10;
        }
    }

    public static void push(InfoEntry entry) {
        push(entry, nextID++);
    }

    public static void push(InfoEntry entry, int id) {
        inbox.put(id, entry);
    }

    public static class InfoEntry implements Comparable<InfoEntry> {
        Component component;
        long start;
        int millis;

        public InfoEntry(Component component, int millis) {
            this.component = component;
            this.millis = millis;
            this.start = System.currentTimeMillis();
        }

        public InfoEntry(Component component) {
            this(component, 3000);
        }

        @Override
        public int compareTo(InfoEntry other) {
            return Integer.compare(this.millis, other.millis);
        }
    }
}
