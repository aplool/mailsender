package com.aplool.mail.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by leokao on 11/29/2016.
 */
public class SmtpServerFinderTest {
    private SmtpServerFinder smtpServerFinder = new SmtpServerFinder();

    @Before
    public void initData() {
        smtpServerFinder.setStartIpAddress("192.168.1.1");
        smtpServerFinder.setEndIpAddress("192.168.1.254");
    }

    //@Test
    public void testRun() throws Exception {
        smtpServerFinder.startSearch();
    }

}
