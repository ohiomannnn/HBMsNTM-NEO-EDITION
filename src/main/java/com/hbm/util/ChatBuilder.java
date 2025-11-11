package com.hbm.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ChatBuilder {

    private final MutableComponent root;
    private MutableComponent last;

    private ChatBuilder(String text) {
        this.root = Component.literal(text);
        this.last = this.root;
    }

    public static ChatBuilder start(String text) {
        return new ChatBuilder(text);
    }

    public static ChatBuilder startTranslation(String key, Object... args) {
        ChatBuilder builder = new ChatBuilder("");
        return builder.nextTranslation(key, args);
    }

    public ChatBuilder next(String text) {
        MutableComponent append = Component.literal(text);
        this.last.append(append);
        this.last = append;
        return this;
    }

    public ChatBuilder nextTranslation(String key, Object... args) {
        MutableComponent append = Component.translatable(key, args);
        this.last.append(append);
        this.last = append;
        return this;
    }

    public ChatBuilder color(ChatFormatting format) {
        this.last.setStyle(this.last.getStyle().withColor(format));
        return this;
    }

    /** Will recursively go over all IChatComponents added to the root and then set the style */
    public ChatBuilder colorAll(ChatFormatting format) {
        List<MutableComponent> list = new ArrayList<>();
        list.add(root);

        ListIterator<MutableComponent> it = list.listIterator();
        while (it.hasNext()) {
            MutableComponent comp = it.next();
            comp.setStyle(comp.getStyle().withColor(format));
            for (Component sibling : comp.getSiblings()) {
                if (sibling instanceof MutableComponent mc) {
                    it.add(mc);
                }
            }
        }
        return this;
    }

    public MutableComponent flush() {
        return this.root;
    }
}