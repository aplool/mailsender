package com.aplool.macro;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by longtai on 2016/11/21.
 */
public class RandomNumber extends Marco {
    static String DEFAULT_NUMBER = "0";

    public RandomNumber(MarcoExecutor executor){
        super(executor);
    }
    public RandomNumber(MarcoExecutor executor, String pattern){
        super(executor,pattern);
    }
    @Override
    protected void init() {

    }

    @Override
    public String generate() {
        return DEFAULT_NUMBER;
    }


    @Override
    public String generate(String parameter) {
        if ("".equals(parameter)) return DEFAULT_NUMBER;

        String[] ss = parameter.split("-");
        if (ss.length < 2) return generate();
        int min = Integer.parseInt(ss[0]);
        int max = Integer.parseInt(ss[1]);
        int result = ThreadLocalRandom.current().nextInt(min, max + 1);

        return String.valueOf(result);
    }
}
