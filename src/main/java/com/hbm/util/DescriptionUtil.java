package com.hbm.util;

import com.hbm.items.special.PolaroidItem;
import com.hbm.util.i18n.I18nUtil;

public class DescriptionUtil {

    public static String[] getDescriptionWithP11(String unlocalizedName) {
        String wDesc = unlocalizedName + ".desc";

        if (PolaroidItem.polaroidID == 11) {
            String keyP11 = wDesc + ".P11";
            String[] arrP11 = I18nUtil.resolveKeyArray(keyP11);

            if (!arrP11[0].equals(keyP11)) {
                return arrP11;
            }
        }
        return I18nUtil.resolveKeyArray(wDesc);
    }
}
