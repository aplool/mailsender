package com.aplool.mail.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Created by longtai on 2016/11/21.
 */
public class RegularGeneratorTest {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Test
    public void testGenerate() throws Exception {
        log.info("Pattern [0-9][a-zA-Z] : {}", new RegularGenerator("[0-9][a-zA-Z]").generate());
        log.info("Pattern [a-z, A-Z, 0-9] : {}", new RegularGenerator("[a-zA-Z0-9]").generate());
        log.info("Pattern [u]{10,25} : {}",new RegularGenerator("[u]{10,25}").generate());
        log.info("Pattern [0-255].[0-255].[0-255].[0-255] : {}",new RegularGenerator("[0-255].[0-255].[0-255].[0-255]").generate());
    }
}