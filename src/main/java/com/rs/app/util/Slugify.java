package com.rs.app.util;

import java.text.Normalizer;

public class Slugify {
    public static String slug(String text){
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("\\p{P}", "")
                .trim()
                .replaceAll("\\s+", "-")
                .replace("ł", "l")
                .replace("Ł", "L")
                .toLowerCase();
    }
}
