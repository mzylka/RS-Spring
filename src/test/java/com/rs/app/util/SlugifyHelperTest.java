package com.rs.app.util;

import org.junit.jupiter.api.Test;

public class SlugifyHelperTest {
    @Test
    public void testSlugifier(){
        String slug = Slugify.slug("Zażółcić jęźlą gąźń łękiętćżźio!!!");
        System.out.println(slug);
    }
}
