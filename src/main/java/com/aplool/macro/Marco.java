package com.aplool.macro;

/**
 * Created by longtai on 2016/11/21.
 */
public abstract class Marco {
    protected MarcoExecutor mExecutor;
    String expression;

    public Marco(MarcoExecutor executor){
        mExecutor = executor;
        this.expression = null;
        init();
    }
    public Marco(MarcoExecutor executor,String expression){
        mExecutor = executor;
        this.expression = expression;
        init();
    }

    public String getExpression(){
        return this.expression;
    }

    protected void init(){};

    abstract public String generate();
    public String generate(String parameter){
        return "";
    }
}
