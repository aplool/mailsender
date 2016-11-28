package com.aplool.macro;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.regex.Matcher;

import static org.junit.Assert.*;

/**
 * Created by longtai on 2016/11/22.
 */
public class MarcoExecutorTest {
    Logger log = LoggerFactory.getLogger(MarcoExecutorTest.class);

    MarcoExecutor excutor;

    @Before
    public void setUp() throws Exception {
        excutor = new MarcoExecutor();
    }

    static String MARCO_MACHER_TEST_STRING = "ABSD %TEST YOU %TEST_PAT woeeuc %TEST_PP[10-20]wewe";

    @Test
    public void findMacher() throws Exception {

        Matcher macher = excutor.findMacher(MARCO_MACHER_TEST_STRING);
        while(macher.find()){
            log.info("Start : {}", macher.start());
            log.info("End   : {}", macher.end());
            log.info("NAME  : {}", macher.group());
        }
    }


    static String[] RESULT_MARCO_NAMES = new String[]{
            "%RND_LC_CHAR",
            "%RND_UC_CHAR",
            "%RND_DIGIT",
            "%RND_CHAR",
            "%RND_DIGIT[10-20]",
            "%RND_NUMBER[0-255]",
            "%RND_IP",
            "%FIRST_NAME",
            "%RND_FROM_DOMAIN",
            "%FROM_EMAIL",
    };

    @Test
    public void testExecuteMarco() throws Exception{
        Arrays.stream(RESULT_MARCO_NAMES).forEach(marco ->
                log.info("Marco : {} -> {}",marco,excutor.executeMarco(marco))
        );
    }

    static String[] TEST_EXPRESSIONS = new String[]{
        "%RND_NUMBER[0-255].%RND_NUMBER[0-255].%RND_NUMBER[0-255].%RND_NUMBER[0-255]"
    };
    @Test public void testExecute() throws Exception{
        Arrays.stream(TEST_EXPRESSIONS).forEach(expression -> {
            log.info("Expression : {}", expression);
            log.info("Result : {}", excutor.execute(expression));
        });
    }

}