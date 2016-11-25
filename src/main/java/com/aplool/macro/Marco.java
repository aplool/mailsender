package com.aplool.macro;

import java.util.regex.Pattern;

/**
 * Created by longtai on 2016/11/21.
 */
public abstract class Marco {
    protected MarcoExecutor mExecutor;
    String mExpression;

    public Marco(MarcoExecutor executor){
        mExecutor = executor;
        mExpression = null;
        init();
    }
    public Marco(MarcoExecutor executor,String pattern){
        mExecutor = executor;
        mExpression = pattern;
        init();
    }

    public String getExpression(){
        return mExpression;
    }

    abstract protected void init();

    abstract public String generate();
    abstract public String generate(MarcoExecutor executor);
    public String generate(String parameter){
        return "";
    }
}
