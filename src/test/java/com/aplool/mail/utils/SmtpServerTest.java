package com.aplool.mail.utils;

import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by leokao on 11/22/2016.
 */
public class SmtpServerTest {
    private static final Logger log = LoggerFactory.getLogger(SmtpServerTest.class);
    private SmtpServer mSmtpServer = null;


    @Before
    public void initData() {
        URL url = this.getClass().getClassLoader().getResource("mailHost.config");
        MailHostConfig mailHostConfig = new MailHostConfig(url.getPath());
        mSmtpServer = new SmtpServer(mailHostConfig);
        url = this.getClass().getClassLoader().getResource("mailHeader.config");
        MailHeaderConfig mailHeaderConfig = new MailHeaderConfig(url.getPath());
        mSmtpServer.setMailHeaderConfig(mailHeaderConfig);
    }

    //@Test
    public void testSmtpServer() {
        log.info("{} isConnectSMTP {}", mSmtpServer.getServerIP() + "-" + mSmtpServer.getServerAddress(), mSmtpServer.testReachable());
        Boolean testResult = mSmtpServer.checkSmtp(false, true);
        log.info("{} isConnectSMTP {}", mSmtpServer.getServerIP() + "-" + mSmtpServer.getServerAddress(), testResult);
        assertEquals(true, testResult);
    }

    @Test
    public void testSmtpList() throws Exception{
        Path file = Paths.get(getClass().getResource("/smtp.txt").toURI());
         List<String> ips = Files.lines(file, Charset.defaultCharset())
            .filter(line -> !line.isEmpty())
            .collect(Collectors.toList());
        ips.forEach(ip->{
            log.info("IP {} is Open : {}", ip, SmtpServer.isMailRelayable(ip));
        });
    }
}
