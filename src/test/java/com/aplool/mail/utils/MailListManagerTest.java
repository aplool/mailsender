package com.aplool.mail.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by longtai on 2017/2/3.
 */
public class MailListManagerTest {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Test
    public void test() throws Exception {
        MailListManager  mailManager = new MailListManager(getClass().getResource("/mailToList.txt").getFile(),2);
        while(mailManager.isNext()){
            log.debug("Has Next : {}", mailManager.isNext());
            List<String> mails = mailManager.next();
            log.debug("Mail Size : {}", mails.size());
            mails.stream().forEach(line->log.info("\t Email : {}",line));
        }

    }
}
