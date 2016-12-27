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

    //@Test
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
        boolean sendResult = false;
        try {
            List<String> mailToList = MailAgent.loadMailToFromFile(this.getClass().getClassLoader().getResource("mailToList.txt").getPath().toString());
            for (String mailToEmail : mailToList) {
                MailAgent mailAgent = new MailAgent(mMailHostConfig);
                mailAgent.setMailHeaderConfig(mMailHeaderConfig);
                //mailAgent.setMarcoPath(this.getClass().getClassLoader().getResource("marco").getPath());

                MailItem mailItem = new MailItem();
                MailAddress toEmail = new MailAddress(mailToEmail, mailToEmail);
                mailItem.addTo(toEmail);
                mailItem.contentType = EmailConstants.TEXT_HTML;
                mailItem.message = mailAgent.loadMessageBodyFromFile(this.getClass().getClassLoader().getResource("mailBody.txt").getPath().toString());
                mLogger.info("Mail to: {} , Message Body: {}", mailToEmail, mailItem.message);
                try {
                    sendResult = mailAgent.send(mailItem);
                    Assert.assertEquals(true, sendResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
