package com.mordeninaf.boot.firin.util;

import java.text.NumberFormat;
import java.util.Locale;

public class TextUtils {
    private static final String TURKISH_CURRENCY_SYMBOL = "₺";

    private TextUtils() {}

    public static String truncZeroFromPrice(Double price) {
        if (price == null)
            return "";
        if (price > price.intValue()) {
            return String.valueOf(price);
        } else {
            return String.valueOf(price.intValue());
        }
    }

    public static String truncAdres(String adres) {
        if (adres == null)
            return "";
        String temp = adres.trim();
        if (temp.length() > Parameters.TRUNC_ADRES) {
            return temp.substring(0, Parameters.TRUNC_ADRES) + "..";
        }
        return temp;
    }

    public static String convertTurkishLira(Number num) {
        if (num == null)
            return "";
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("tr_TR"));
        numberFormat.setMaximumFractionDigits(1);
        String tmpResult = String.format("%s %s",TURKISH_CURRENCY_SYMBOL, numberFormat.format(num));
        return String.format("%7s", tmpResult).replace(" ", "\u00A0");
    }
}
