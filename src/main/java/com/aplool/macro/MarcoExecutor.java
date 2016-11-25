package com.aplool.macro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by longtai on 2016/11/22.
 */
public class MarcoExecutor {

    static Logger log = LoggerFactory.getLogger(MarcoExecutor.class);
    static String DEFAULT_RESULT = "";

    Pattern mMarcoPattern = Pattern.compile("(%[A-Z_]+)(\\[.[^\\]]*\\])?");
    Pattern mMarcoNamePattern = Pattern.compile("(%[A-Z_]+)");
    Pattern mMarcoParameterPattern = Pattern.compile("(\\[.*\\])");

    Hashtable<String, Marco> mMarcos = new Hashtable<String,Marco>();

    public MarcoExecutor()throws Exception{
        initStandardMarco();
    }
    private void initStandardMarco() throws Exception{
        mMarcos.put("RND_LC_CHAR",new RandomChar(this,"[a-z]"));
        mMarcos.put("RND_UC_CHAR",new RandomChar(this,"[A-Z]"));
        mMarcos.put("RND_DIGIT",new RandomChar(this,"[0-9]"));
        mMarcos.put("RND_CHAR",new RandomChar(this,"[0-9a-zA-Z]"));
        mMarcos.put("RND_NUMBER",new RandomNumber(this));
//        mMarcos.put("RND_IP", new ExpressionMarco(this,"%RND_NUMBER[0-255].%RND_NUMBER[0-255].%RND_NUMBER[0-255].%RND_NUMBER[0-255]"));
//        mMarcos.put("FIRST_NAME", new RandomList(this,"/marco/FIRST_NAME"));
//  mMarcos.put("RND_FROM_DOMAIN", new RandomList(this,"/marco/RND_FROM_DOMAIN"));
//        mMarcos.put("FROM_EMAIL", new RandomList(this,"/marco/FROM_EMAIL"));

    }

    public void addMarco(String marcoName, Marco marco){
        mMarcos.put(marcoName, marco);
    }
    public Matcher findMacher(String content) {
        return mMarcoPattern.matcher(content);
    }

    public String execute(String expression){
        StringBuilder result = new StringBuilder();
        Matcher marcos = mMarcoPattern.matcher(expression);
        int lastCharIndex = 0;
        while(marcos.find()){
            result.append(expression.substring(lastCharIndex,marcos.start()));
            result.append(executeMarco(marcos.group()));
            lastCharIndex =  marcos.end();
        }

        if(expression.length()>lastCharIndex ){
            result.append(expression.substring(lastCharIndex));
        }

        return result.toString();
    }



    public String executeMarco(String expression){
        Matcher marcos = mMarcoPattern.matcher(expression);
        if(!marcos.find()) return expression;

        String marcoName = getMarcoName(expression);
        String marcoParameter = getMacroParameter(expression);
        if(!mMarcos.containsKey(marcoName)) return expression;
        return mMarcos.get(marcoName).generate(marcoParameter);
    }

    private String getMarcoName(String expression){
        Matcher marco = mMarcoNamePattern.matcher(expression);
        if(marco.find())
            return marco.group().replaceFirst("%","");
        return "";
    }

    private String getMacroParameter(String expression){
        Matcher marco = mMarcoParameterPattern.matcher(expression);
        if(marco.find())
            return marco.group().replaceFirst("\\[","").replaceFirst("\\]","").trim();
        return "";
    }

}
