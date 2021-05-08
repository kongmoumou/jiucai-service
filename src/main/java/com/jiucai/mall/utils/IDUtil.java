package com.jiucai.mall.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IDUtil {
    public static Long getID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            result += random.nextInt(10);
        }
        Long l = Long.parseLong(newDate + result);
        return l;
    }
}
