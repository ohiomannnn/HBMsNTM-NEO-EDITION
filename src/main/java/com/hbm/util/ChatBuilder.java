package com.hbm.util;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ChatBuilder {

    private final MutableComponent text;
    private MutableComponent last;

    private ChatBuilder(String text) {
        this.text = Component.literal(text);
        this.last = this.text;
    }

    public static ChatBuilder start(String text) {
        return new ChatBuilder(text);
    }

    public static ChatBuilder startTranslation(String key, Object... args) {
        return new ChatBuilder("").nextTranslation(key, args);
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

    public ChatBuilder colorAll(ChatFormatting format) {
        List<MutableComponent> list = new ArrayList<>();
        list.add(this.text);

        ListIterator<MutableComponent> it = list.listIterator();
        while (it.hasNext()) {
            MutableComponent component = it.next();
            component.setStyle(component.getStyle().withColor(format));
            for (Component sibling : component.getSiblings()) {
                if (sibling instanceof MutableComponent mc) {
                    it.add(mc);
                }
            }
        }
        return this;
    }

    public MutableComponent flush() {
        return this.text;
    }
}
