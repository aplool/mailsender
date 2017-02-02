package com.aplool.mail.utils;

import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import org.apache.commons.mail.EmailConstants;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

/**
 * Created by leokao on 11/17/2016.
 */
public class MailAgentTest {
    private static final Logger mLogger = LoggerFactory.getLogger(MailAgentTest.class);
    private MailHostConfig mMailHostConfig = new MailHostConfig();
    private MailHeaderConfig mMailHeaderConfig = null;

    @Before
    public void initData() {
        URL url = this.getClass().getClassLoader().getResource("mailHost.config");
        mMailHostConfig = new MailHostConfig(url.getPath());
        url = this.getClass().getClassLoader().getResource("mailHeader.config");
        mMailHeaderConfig = new MailHeaderConfig(url.getPath());
    }


}
