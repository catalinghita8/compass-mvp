package com.inspiringteam.transferxcompass.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FormatUtils {
    public static String getStringFromDouble(double d) {
        NumberFormat formatter = new DecimalFormat("###.#######");
        return formatter.format(d);
    }
}
