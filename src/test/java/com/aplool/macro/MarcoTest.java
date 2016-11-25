package com.aplool.macro;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by longtai on 2016/11/21.
 */
public class MarcoTest {
    Logger log = LoggerFactory.getLogger(MarcoTest.class);
    @Before
    public void setUp() throws Exception {

    }


    @Test
    public void testRandomNumber() throws Exception {
        log.info("RandomNumber generate(0-255) : {}", new RandomNumber(null).generate("0-255"));
    }

    @Test
    public void testRandomChar() throws Exception {
        RandomChar RND_LC_CHAR = new RandomChar(null,"[a-z]");
        log.info("RND_LC_CHAR generate : {}", RND_LC_CHAR.generate());
        log.info("RND_LC_CHAR generate(15-20) : {}", RND_LC_CHAR.generate("15-20"));
        RandomChar RND_UC_CHAR = new RandomChar(null,"[A-Z]");
        log.info("RND_UC_CHAR generate : {}", RND_UC_CHAR.generate());
        log.info("RND_UC_CHAR generate(15-20) : {}", RND_UC_CHAR.generate("15-20"));
        RandomChar RND_DIGIT = new RandomChar(null,"[0-9]");
        log.info("RND_DIGIT generate : {}", RND_DIGIT.generate());
        log.info("RND_DIGIT generate(15-20) : {}", RND_DIGIT.generate("15-20"));
        RandomChar RND_CHAR = new RandomChar(null,"[0-9a-zA-Z]");
        log.info("RND_CHAR generate : {}", RND_CHAR.generate());
        log.info("RND_CHAR generate(15-20) : {}", RND_CHAR.generate("15-20"));
    }
}