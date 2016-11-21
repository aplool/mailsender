package com.aplool.mail.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by longtai on 2016/11/21.
 */
public class RegularGeneratorTest {
    @Test
    public void testGenerate() throws Exception {
        System.out.println(new RegularGenerator().generate("[0-9][a-zA-Z]"));
    }
}