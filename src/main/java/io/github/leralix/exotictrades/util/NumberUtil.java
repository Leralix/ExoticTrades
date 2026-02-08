package io.github.leralix.exotictrades.util;

import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

public class NumberUtil {

    private NumberUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static int nbDigits;

    public static void init(){
        nbDigits = ConfigUtil.getCustomConfig(ConfigTag.MAIN).getInt("nbDigits", 2);
    }

    public static double roundWithDigits(double value){
        return Math.round(value * Math.pow(10, nbDigits)) / Math.pow(10, nbDigits);
    }
}
