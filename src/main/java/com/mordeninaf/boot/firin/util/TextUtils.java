package com.mordeninaf.boot.firin.util;

public class TextUtils {

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
}
