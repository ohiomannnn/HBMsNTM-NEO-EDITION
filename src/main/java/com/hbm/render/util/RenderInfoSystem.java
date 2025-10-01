package com.hbm.render.util;

import com.hbm.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
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

        if (messages.isEmpty())
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;
        GuiGraphics graphics = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        List<InfoEntry> entries = new ArrayList<>(messages.values());
        Collections.sort(entries);

        int longest = 0;
        for (InfoEntry entry : messages.values()) {
            int length = mc.font.width(entry.component);
            if (length > longest)
                longest = length;
        }

        int mode = ClientConfig.INFO_POSITION.get();
        int pX = mode == 0 ? 15 : mode == 1 ? (screenWidth - longest - 15) : mode == 2 ? (screenWidth / 2 + 7) : (screenWidth / 2 - longest - 6);
        int pZ = mode == 0 ? 15 : mode == 1 ? 15 : (screenHeight / 2 + 7);

        pX += ClientConfig.INFO_OFFSET_HORIZONTAL.get();
        pZ += ClientConfig.INFO_OFFSET_VERTICAL.get();

        int side = pX + 5 + longest;
        int height = messages.size() * 10 + pZ + 2;

        graphics.fill(pX - 5, pZ - 5, side, height, 0x80000000);

        int off = 0;
        long now = System.currentTimeMillis();

        for (InfoEntry entry : messages.values()) {
            int elapsed = (int) (now - entry.start);
            int alpha = Mth.clamp(510 * (entry.millis - elapsed) / entry.millis, 5, 255);

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
