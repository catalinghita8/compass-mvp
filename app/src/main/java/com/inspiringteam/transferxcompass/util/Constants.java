package com.inspiringteam.transferxcompass.util;

import android.util.Pair;

public class Constants {
    public static int LOCATION_DATA_SOURCE = 0;
    public static int COMPASS_DATA_SOURCE = 1;
    public static int REMOTE_DATA_SOURCE = 2;

    public static String DESTINATION_API_BASE_URL = "http://transferx.ddns.net:3000/";

    public static Pair<Double, Double> SAMPLE_COORDINATES = new Pair<>(44.346328, 25.961115);
    public static Pair<Double, Double> SAMPLE_COORDINATES2 = new Pair<>(4.345839, 2.959957);
}
