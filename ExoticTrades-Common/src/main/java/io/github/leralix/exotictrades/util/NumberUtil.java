package io.github.leralix.exotictrades.util;

import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

public class NumberUtil {

    private NumberUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static int nbDigitsStatic;

    public static void init(int nbDigits){
        nbDigitsStatic = nbDigits;
    }

    public static double roundWithDigits(double value){
        return Math.round(value * Math.pow(10, nbDigitsStatic)) / Math.pow(10, nbDigitsStatic);
    }
}
