package com.aplool.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by longtai on 2016/12/1.
 */
public class Main {
    static Logger log = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        log.info("Mail Send Start :");

        String defaultPath = (args.length==0)?System.getProperty("user.dir")+System.getProperty("file.separator"):args[0];
        MainThread main = null;
        try {
            main = new MainThread(defaultPath);
            main.start();

        } catch (RuntimeException e) {
            log.error(e.getMessage(),e.getCause());
        }

    }
}
