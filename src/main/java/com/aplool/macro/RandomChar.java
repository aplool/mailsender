package com.aplool.macro;

import com.mifmif.common.regex.Generex;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by longtai on 2016/11/21.
 */
public class RandomChar extends Marco {
    Generex mGenerator;

    public RandomChar(MarcoExecutor executor) {
        super(executor);
    }
    public RandomChar(MarcoExecutor executor, String pattern) {
        super(executor,pattern);
    }
    @Override
    protected void init()  {
        mGenerator = new Generex(this.getExpression());
    }


    @Override
    public String generate() {
        return mGenerator.random();
    }

    @Override
    public String generate(MarcoExecutor executor) {
        return mGenerator.random();
    }

    @Override
    public String generate(String parameter) {
        if("".equals(parameter)) return generate();

        String[] ss = parameter.split("-");
        if(ss.length < 2) return generate();
        int min = Integer.parseInt(ss[0]);
        int max = Integer.parseInt(ss[1]);
        int repeatQty = ThreadLocalRandom.current().nextInt(min, max + 1);
        StringBuilder result = new StringBuilder();
        while(repeatQty > 0){
            result.append(generate());
            repeatQty--;
        }
        return result.toString();
    }
}
