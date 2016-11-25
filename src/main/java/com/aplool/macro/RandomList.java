package com.aplool.macro;

import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by longtai on 2016/11/23.
 */
public class RandomList extends Marco {
    List<String> mExpressions;

    public RandomList(MarcoExecutor executor) {
        super(executor);
    }


    @Override
    protected void init() {
//        Path path = Paths.get(getClass().getResource(this.getExpression()).toURI());
//        mExpressions = Files.lines(path, Charset.defaultCharset()).collect(Collectors.toList());
    }

    public void setExpressions(List<String> expressions) {
        if (expressions != null) mExpressions = expressions;
    }

    @Override
    public String generate() {
        String result = "";
        int randomIndex = 0;

        randomIndex = ThreadLocalRandom.current().nextInt(0, mExpressions.size());
        result = mExecutor.execute(mExpressions.get(randomIndex));


        return result;
    }

    @Override
    public String generate(String parameter) {
        return generate();
    }

    @Override
    public String generate(MarcoExecutor executor) {
        return null;
    }
}
