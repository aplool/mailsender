package com.ll.utils.mail;

import com.ll.utils.mail.model.MailAddress;
import com.ll.utils.mail.model.MailHostConfig;
import com.ll.utils.mail.model.MailItem;
import org.apache.commons.mail.EmailConstants;
import org.junit.Test;

/**
 * Created by leokao on 11/17/2016.
 */
public class MailAgentTest {
    @Test
    public void testSendMail() {
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
        mailItem.subject = "TestMail";
        mailItem.message = "Thsi is Test Mail";
        mailAgent.sendMail(mailItem);
    }
}