package com.aplool.mail.utils;

import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import org.apache.commons.mail.EmailConstants;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void testSendMail() {
//        Received: from %RND_IP by %PROXY; %RND_DATE_TIME
//        Message-ID: <%RND_UC_CHAR[20-25]@%RND_FROM_DOMAIN>
//                From: "%FROM_NAME" <%FROM_EMAIL>
//                Reply-To: "%FROM_NAME" <%FROM_EMAIL>
//        %TO_CC_DEFAULT_HANDLER
//        Subject: %SUBJECT
//        Date: %RND_DATE_TIME
//        X-Mailer: %X_MAILER
//        MIME-Version: 2.0
//        Content-Type: multipart/alternative;
//        boundary="--%BOUNDARY"
//        X-Priority: %PRIORITY_NUMBER
//        X-MSMail-Priority: %PRIORITY_STRING
//
//                ----%BOUNDARY
//        Content-Type: %CONTENT_TYPE;
//        Content-Transfer-Encoding: %CONTENT_ENCODING
//
//                %MESSAGE_BODY
//
//                ----%BOUNDARY--

        MailAgent mailAgent = new MailAgent(mMailHostConfig);

        MailItem mailItem = new MailItem();
//        MailAddress fromEmail = new MailAddress("test@mail.com", "Test mailer");
//        mailItem.from = fromEmail;
//        MailAddress toEmail = new MailAddress("test@mail.com", "Test mailer");
//        mailItem.addTo(toEmail);
//        mailItem.subject = "TestMail 測試郵件";
//        mailItem.contentType = EmailConstants.TEXT_HTML;
//        mailItem.message = "Thsi is Test Mail 這是測試郵件";
//        mailAgent.sendMail(mailItem);
        mailItem.contentType = EmailConstants.TEXT_HTML;
        mailItem.message = "<h2>Thsi is Test Mail 這是測試郵件</h2>";
        boolean sendResult = mailAgent.sendMail(mailItem);
        assertEquals(true, sendResult);
    }
}