package com.aplool.mail.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leokao on 12/16/2016.
 */
public class MailHostListFile {
    private static final Logger log = LoggerFactory.getLogger(MailHostListFile.class);

    String filePath = "";
    String hostIPListFile = "mailHostList.txt";
    int mailserverIndex =0;
    List<String> ips = null;

    public MailHostListFile(String filePath) {
        this.filePath = filePath;
        initMailServerList();
    }

    private void initMailServerList(){
        Path file = Paths.get(filePath + "/" + hostIPListFile);

        try {
            ips = Files.lines(file, Charset.defaultCharset())
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
            mailserverIndex =0;
            log.debug("MailServerList with {} ips", ips.size());
        } catch (IOException e) {
            log.error("MailServerList File : {} is initial error.", file.toUri().getPath());
            log.error("MailList init Error",e.getCause());
            ips = null;
        }
    }

    public String getNewHostIP() {
        String result = "";
        if(ips == null) return result;
        if(mailserverIndex>=ips.size()) return result;
        result = ips.get(mailserverIndex);
        mailserverIndex ++;
        return result;
    }

    public String getNextReachableHost() {
        String newIP="";
        while((newIP = getNewHostIP()) !=""){
            if(SmtpServer.isMailRelayable(newIP)) break;
        }

        return newIP;
    }
}
