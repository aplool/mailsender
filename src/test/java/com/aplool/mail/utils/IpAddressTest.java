package com.aplool.mail.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Created by leokao on 11/25/2016.
 */
public class IpAddressTest {
    private static final Logger mLogger = LoggerFactory.getLogger(IpAddressTest.class);
    private IpAddress mIpAddress = null;

    @Before
    public void initData() {
        mIpAddress = new IpAddress("192.168.1.254");
    }

    @Test
    public void testToString() throws Exception {
        Assert.assertEquals("192.168.1.254", mIpAddress.toString());
    }

    @Test
    public void testEquals() throws Exception {
        Assert.assertEquals("192.168.1.254", mIpAddress.toString());
        Assert.assertEquals(new IpAddress("192.168.1.254"), mIpAddress.toString());
    }


    @Test
    public void testNextIP() throws Exception {
        IpAddress ipAddress = mIpAddress;
        Assert.assertEquals("192.168.1.255", mIpAddress.nextIP().toString());
    }

    @Test
    public void nextServerIP() throws Exception {
        IpAddress ipAddress = mIpAddress;
        Assert.assertEquals("192.168.2.1", mIpAddress.nextServerIP().toString());
    }
}