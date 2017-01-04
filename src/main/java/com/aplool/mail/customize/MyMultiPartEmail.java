package com.aplool.mail.customize;

import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Created by leokao on 11/21/2016.
 */
public class MyMultiPartEmail extends MultiPartEmail {
    private static final Logger log = LoggerFactory.getLogger(MyMultiPartEmail.class);
    private String mMessageId;

    @Override
    protected MimeMessage createMimeMessage(Session aSession) {

        MyMimeMessage mailMessageEx = new MyMimeMessage(aSession);
        mailMessageEx.setMessageId(this.mMessageId);
        return mailMessageEx;
    }

    public String getMessageId() {
        return this.mMessageId;
    }

    public void setMessageId(String mMessageId) {
        this.mMessageId = mMessageId;
    }

    public String toString() {
        StringBuilder r = new StringBuilder();
        r.append(String.format("Mail Header ]\n"));
        this.headers.forEach((k, v) -> {
            r.append(String.format("\t %s : %s \n", k, v));
        });
        r.append(String.format("Mail From : %s \n", this.getFromAddress().toString()));
        r.append(String.format("["));
        return r.toString();
    }
    }
