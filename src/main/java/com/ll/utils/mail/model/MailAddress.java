package com.ll.utils.mail.model;

/**
 * Created by leokao on 11/18/2016.
 */
public class MailAddress {
    public String mailAddress = "";
    public String mailUser = "";

    public MailAddress() {}

    public MailAddress(String mailAddress, String mailUser) {
        this.mailAddress = mailAddress;
        this.mailUser = mailUser;
    }
}
