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
    @OnlyIn(Dist.CLIENT)
    public String resolveKey(String s, Object... args) {
        return I18n.get(s, args);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String[] resolveKeyArray(String s, Object... args) {
        return resolveKey(s, args).split("\\$");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<String> autoBreakWithParagraphs(Object fontRenderer, String text, int width) {
        String[] paragraphs = text.split("\\$");
        List<String> lines = new ArrayList<>();

        for (String paragraph : paragraphs) {
            lines.addAll(autoBreak(fontRenderer, paragraph, width));
        }

        return lines;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<String> autoBreak(Object o, String text, int width) {
        Font fontRenderer = (Font) o;
        List<String> lines = new ArrayList<>();
        //split the text by all spaces
        String[] words = text.split(" ");

        //add the first word to the first line, no matter what
        lines.add(words[0]);
        //starting indent is the width of the first word
        int indent = fontRenderer.width(words[0]);

        for (int w = 1; w < words.length; w++) {
            //increment the indent by the width of the next word + leading space
            indent += fontRenderer.width(" " + words[w]);

            //if the indent is within bounds
            if (indent <= width) {
                //add the next word to the last line (i.e. the one in question)
                String last = lines.getLast();
                lines.set(lines.size() - 1, last + (" " + words[w]));
            } else {
                //otherwise, start a new line and reset the indent
                lines.add(words[w]);
                indent = fontRenderer.width(words[w]);
            }
        }

        return lines;
    }
}
