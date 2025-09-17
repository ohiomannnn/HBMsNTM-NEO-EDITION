package com.hbm.util.i18n;

import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class I18nUtil {

    public static String resolveKey(String key, Object... args) {
        return I18n.get(key, args);
    }

    public static String format(String key, Object... args) {
        return I18n.get(key, args);
    }

    public static String[] resolveKeyArray(String key, Object... args) {
        return I18n.get(key, args).split("\\$");
    }

    public static List<String> autoBreakWithParagraphs(Object fontRenderer, String text, int width) {
        if (fontRenderer instanceof Font font) {
            List<String> result = new ArrayList<>();

            String[] paragraphs = text.split("\\$");

            for (String paragraph : paragraphs) {

                List<FormattedCharSequence> lines = font.split(Component.literal(paragraph), width);

                for (FormattedCharSequence seq : lines) {
                    StringBuilder sb = new StringBuilder();
                    seq.accept((index, style, codePoint) -> {
                        sb.appendCodePoint(codePoint);
                        return true;
                    });
                    result.add(sb.toString());
                }
            }

            return result;
        }
        return List.of(text);
    }
    public static List<String> autoBreak(Object fontRenderer, String text, int width) {
        if (fontRenderer instanceof Font font) {
            List<FormattedCharSequence> lines = font.split(Component.literal(text), width);
            return lines.stream()
                    .map(seq -> {
                        StringBuilder sb = new StringBuilder();
                        seq.accept((index, style, codePoint) -> {
                            sb.appendCodePoint(codePoint);
                            return true;
                        });
                        return sb.toString();
                    })
                    .toList();
        }
        return List.of(text);
    }
}
