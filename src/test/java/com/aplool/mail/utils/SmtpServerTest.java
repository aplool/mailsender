package com.aplool.mail.utils;

import com.aplool.mail.model.MailHostConfig;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by leokao on 11/22/2016.
 */
public class SmtpServerTest {
    private static final Logger mLogger = LoggerFactory.getLogger(SmtpServerTest.class);
    private SmtpServer mSmtpServer = null;

    @Before
    public void initData() {
        URL url = this.getClass().getClassLoader().getResource("mailHost.config");
        MailHostConfig mailHostConfig = new MailHostConfig(url.getPath());
        mSmtpServer = new SmtpServer(mailHostConfig);
    }

    @Test
    public void testSmtpServer() {
        mLogger.info("{} isReachable {}", mSmtpServer.getServerIP(), mSmtpServer.testReachable());
        mLogger.info("{} isConnectSMTP {}", mSmtpServer.getServerAddress(), mSmtpServer.checkSmtp(true));
    }
}