package com.hbm.util.i18n;

import java.util.List;

public interface ITranslate {
    String resolveKey(String key, Object... args);
    String[] resolveKeyArray(String key, Object... args);
    List<String> autoBreakWithParagraphs(Object fontRenderer, String text, int width);
    List<String> autoBreak(Object fontRenderer, String text, int width);
}