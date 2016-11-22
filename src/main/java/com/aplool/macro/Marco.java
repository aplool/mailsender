package com.aplool.macro;

/**
 * Created by longtai on 2016/11/21.
 */
public abstract class Marco {
    String mPattern;
    public Marco(){
        mPattern = null;
        init();
    }
    public Marco(String pattern){
        mPattern = pattern;
        init();
    }

    public String getPattern(){
        return mPattern;
    }

    abstract protected void init();

    abstract public String generate();
    public String generate(String parameter){
        return "";
    }
}
