package com.ll.utils.mail.model;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailConstants;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leokao on 11/18/2016.
 */
public class MailItem {
    public MailAddress from;
    public List<MailAddress> to = new ArrayList<>();
    public List<MailAddress> cc = new ArrayList<>();
    public List<MailAddress> bcc = new ArrayList<>();
    public String subject = "Mail Subject";
    public String message = "Test mail";
    public String contentType = EmailConstants.TEXT_PLAIN;
    public List<AttachmentItem> attachmentItems = new ArrayList<>();
    public List<EmbedItem> embedItems = new ArrayList<>();

    public boolean isMultipart() {
        return !attachmentItems.isEmpty();
    }

    public void addTo(MailAddress mailAddress) {
        this.to.add(mailAddress);
    }

    public void addCc(MailAddress mailAddress) {
        this.cc.add(mailAddress);
    }

    public void addBcc(MailAddress mailAddress) {
        this.bcc.add(mailAddress);
    }

    public void addAttachment(AttachmentItem attachmentItem) {
        this.attachmentItems.add(attachmentItem);
    }

    public void addEmbed(EmbedItem embedItem) {
        this.embedItems.add(embedItem);
    }

    public class AttachmentItem{
        public String path = "";
        public URL url = null;
        public String disposition = "";
        public String description = "";
        public String name = "mail attachment";
    }

    public class EmbedItem {
        public URL url;
        public String Description;
    }
}
