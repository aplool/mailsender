package com.aplool.macro;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Created by longtai on 2016/11/28.
 */
public class RandomDateTimeTest {
    Logger log = LoggerFactory.getLogger(RandomDateTimeTest.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testRandomDateTime() throws Exception{

        Marco RND_DATE_TIME = new RandomDateTime(null);
        log.debug("RND_DATE_TIME : {}", RND_DATE_TIME.generate());
        log.info("RND_DATE_TIME : {}", RND_DATE_TIME.generate(""));
        log.info("RND_DATE_TIME(yyyy/MM/dd) : {}", RND_DATE_TIME.generate("yyyy/MM/dd"));

    }

}
