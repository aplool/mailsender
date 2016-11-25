package com.aplool.macro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;

/**
 * Created by longtai on 2016/11/22.
 */
public class ExpressionMarco extends Marco {

    static Logger log = LoggerFactory.getLogger(ExpressionMarco.class);

    public ExpressionMarco(MarcoExecutor executor, String pattern){
        super(executor,pattern);
    }
    @Override
    protected void init(){

    }

    @Override
    public String generate() {
        return null;
    }

    @Override
    public String generate(String parameter) {
        return mExecutor.execute(this.getExpression());
    }

    @Override
    public String generate(MarcoExecutor executor) {
        log.info("Expression : {}", this.getExpression());
         return executor.execute(this.getExpression());
    }
}
