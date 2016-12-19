package com.aplool.mail.utils;

import com.aplool.mail.model.MailHeaderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by leokao on 11/29/2016.
 */
public class SmtpServerFinder {
            private static final Logger mLogger = LoggerFactory.getLogger(SmtpServerFinder.class);
            private IpAddress startIpAddress = new IpAddress("1.0.0.1");
            private IpAddress endIpAddress = new IpAddress("1.0.0.2");

        public void startSearch() {
            URL url = this.getClass().getClassLoader().getResource("mailHeader.config");
            MailHeaderConfig mailHeaderConfig = new MailHeaderConfig(url.getPath());
            IpAddress curIpAddress = startIpAddress;
            while (!curIpAddress.equals(endIpAddress.nextServerIP())) {
                SmtpServer smtpServer = new SmtpServer(curIpAddress.toString());
                smtpServer.setMailHeaderConfig(mailHeaderConfig);
            boolean testSmtp = smtpServer.testReachable();
            if (testSmtp) {
                mLogger.info("{} => Reachable: {}", curIpAddress.toString(), testSmtp);
                testSmtp = smtpServer.checkSmtp(false, true);
                if (testSmtp) {
                    mLogger.info("{} => Smtp Test: {}", curIpAddress.toString(), testSmtp);
                }
            }
            System.out.println(curIpAddress.toString());
            curIpAddress = curIpAddress.nextServerIP();
        }
    }

    public IpAddress getStartIpAddress() {
        return startIpAddress;
    }

    public void setStartIpAddress(String startIpAddress) {
        this.startIpAddress = new IpAddress(startIpAddress);
    }

    public IpAddress getEndIpAddress() {
        return endIpAddress;
    }

    public void setEndIpAddress(String endIpAddress) {
        this.endIpAddress = new IpAddress(endIpAddress);
    }
}
