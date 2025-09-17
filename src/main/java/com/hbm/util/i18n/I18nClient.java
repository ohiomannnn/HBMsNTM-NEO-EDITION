package com.hbm.util.i18n;

import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class I18nClient implements ITranslate {

    @Override
    public String resolveKey(String s, Object... args) {
        return I18n.get(s, args);
    }

    @Override
    public String[] resolveKeyArray(String s, Object... args) {
        return resolveKey(s, args).split("\\$");
    }

    @Override
    public List<String> autoBreakWithParagraphs(Object fontRenderer, String text, int width) {
        String[] paragraphs = text.split("\\$");
        List<String> lines = new ArrayList<>();

        for (String paragraph : paragraphs) {
            lines.addAll(autoBreak(fontRenderer, paragraph, width));
        }

        return lines;
    }

    @Override
    public List<String> autoBreak(Object o, String text, int width) {
        Font font = (Font) o;
        List<String> result = new ArrayList<>();

        // Разбиваем текст на слова
        String[] words = text.split(" ");
        if (words.length == 0) return result;

        StringBuilder currentLine = new StringBuilder(words[0]);
        int indent = font.width(Component.literal(words[0]));

        for (int w = 1; w < words.length; w++) {
            int wordWidth = font.width(Component.literal(" " + words[w]));
            if (indent + wordWidth <= width) {
                currentLine.append(" ").append(words[w]);
                indent += wordWidth;
            } else {
                result.add(currentLine.toString());
                currentLine = new StringBuilder(words[w]);
                indent = font.width(Component.literal(words[w]));
            }
        }
        result.add(currentLine.toString());

        return result;
    }

    private static String sequenceToString(FormattedCharSequence seq) {
        StringBuilder sb = new StringBuilder();
        seq.accept((index, style, codePoint) -> {
            sb.appendCodePoint(codePoint);
            return true;
        });
        return sb.toString();
    }
}
