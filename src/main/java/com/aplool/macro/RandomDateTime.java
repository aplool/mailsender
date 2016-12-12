package com.aplool.macro;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by longtai on 2016/11/28.
 */
public class RandomDateTime extends Marco {


    public RandomDateTime(MarcoExecutor executor) {
        super(executor);
    }

    @Override
    public String generate() {
        Date date = randomDate();
        return date.toString();
    }

    @Override
    public String generate(String parameter) {
        if("".equals(parameter.trim())) return generate();
        SimpleDateFormat spf = new SimpleDateFormat(parameter);
        Date date = randomDate();
        return spf.format(date);
    }

    private Date randomDate(){

        Long randomTime = ThreadLocalRandom.current().nextLong(0,System.currentTimeMillis());

        Date result = new Date(randomTime);
        return result;
    }
}
