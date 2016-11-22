package com.aplool.mail.customize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;

/**
 * Created by leokao on 11/21/2016.
 */
public class MyMimeMessage extends MimeMessage {
    private static final Logger logger = LoggerFactory.getLogger(MyMimeMessage.class);
    private String mMessageId;

    public MyMimeMessage(Session session) {
        super(session);
    }

    public MyMimeMessage(Session session, InputStream is) throws MessagingException {
        super(session, is);
    }

    public MyMimeMessage(MimeMessage source) throws MessagingException {
        super(source);
    }

    protected MyMimeMessage(Folder folder, int msgnum) {
        super(folder, msgnum);
    }

    protected MyMimeMessage(Folder folder, InputStream is, int msgnum) throws MessagingException {
        super(folder, is, msgnum);
    }

    protected MyMimeMessage(Folder folder, InternetHeaders headers, byte[] content, int msgnum) throws MessagingException {
        super(folder, headers, content, msgnum);
    }

    @Override
    protected void updateMessageID() throws MessagingException {
        super.setHeader("Message-ID", mMessageId);
        logger.debug("Class Message" + mMessageId);
    }

    public String getMessageId() {
        return mMessageId;
    }

    public void setMessageId(String mMessageId) {
        try {
            this.mMessageId = mMessageId;
            logger.debug(this.getHeader("Message-Id").toString());
        }catch (Exception e) {};
    }
}
