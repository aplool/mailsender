package com.aplool.mail.utils;


import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import org.apache.commons.mail.EmailConstants;
import org.junit.Test;

/**
 * Created by leokao on 11/17/2016.
 */
public class MailAgentTest {
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
        MailHostConfig mailHostConfig = new MailHostConfig();
        mailHostConfig.setHostAddress("mail.coretronic.com");
        mailHostConfig.setHostPort(25);
        mailHostConfig.setNeedAuth(true);
        mailHostConfig.setUserName("EC");
        mailHostConfig.setUserPassword("Coretronic0232-0217ABCDEF");
        MailAgent mailAgent = new MailAgent(mailHostConfig);
        MailItem mailItem = new MailItem();
        mailItem.contentType = EmailConstants.TEXT_HTML;
        MailAddress fromEmail = new MailAddress("EC@coretronic.com","EC");
        mailItem.from = fromEmail;
        MailAddress toEmail = new MailAddress("leo.kao@coretronic.com","Leo Kao");
        mailItem.addTo(toEmail);
        mailItem.subject = "TestMail 測試郵件";
        mailItem.message = "Thsi is Test Mail 這是測試郵件";
        mailAgent.sendMail(mailItem);
        mailItem.contentType = EmailConstants.TEXT_HTML;
        mailItem.message = "<h2>Thsi is Test Mail 這是測試郵件</h2>";
        mailAgent.sendMail(mailItem);
    }
}