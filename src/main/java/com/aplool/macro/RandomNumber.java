package com.aplool.macro;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by longtai on 2016/11/21.
 */
public class RandomNumber extends Marco {
    @Override
    protected void init() {

    }
    @Override
    public String generate() {
        return "";
    }

    @Override
    public String generate(String parameter) {
        String[] ss = parameter.split("-");
        if(ss.length < 2) return generate();
        int min = Integer.parseInt(ss[0]);
        int max = Integer.parseInt(ss[1]);
        int result = ThreadLocalRandom.current().nextInt(min, max + 1);

        return String.valueOf(result);
    }
}
