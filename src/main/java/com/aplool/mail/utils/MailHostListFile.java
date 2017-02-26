package com.aplool.mail.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    static String ERROR_MESSAGE_MAIL_HOST_EMPTY = "MailServerList is empty. [%s]";
    static String ERROR_MESSAGE_MAIL_HOST_IO = "MailServerList is initial error . [%s]";
    private static final Logger log = LoggerFactory.getLogger(MailHostListFile.class);

    String filePath = "";
    int mailserverIndex =0;
    List<String> ips = null;
    boolean isCycleFetch = true;

    public MailHostListFile(String filePath) throws RuntimeException {
        this.filePath = filePath;
        initMailServerList();
    }

    private void initMailServerList() throws RuntimeException{
        Path file = Paths.get(filePath);

        try {
            ips = Files.lines(file, Charset.defaultCharset())
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
            mailserverIndex =0;
            log.debug("MailServerList with {} ips", ips.size());
        } catch (IOException e) {
            ips = null;
            throw new RuntimeException(String.format(ERROR_MESSAGE_MAIL_HOST_IO,file.toUri().getPath()),e.getCause());
        }
        if(ips.size()==0) throw new RuntimeException(String.format(ERROR_MESSAGE_MAIL_HOST_EMPTY,file.toUri().getPath()));
    }

    public boolean isNext(){
        if(ips == null) return false;
        if(mailserverIndex < (ips.size()-1)) return true;
        return false;
    }
    public synchronized String getNewHostIP() {
        String result = "";
        if(ips == null) return result;
        if(mailserverIndex>=ips.size()){
            if(isCycleFetch){
                mailserverIndex = 0;
            } else {
                return result;
            }
        }
        result = ips.get(mailserverIndex);
        mailserverIndex ++;
        return result;
    }

    public String getNextReachableHost() {
        boolean isGetNextIP = true;
        String newIP = "";
        while(isGetNextIP){
            newIP = getNewHostIP();
            if("".equals(newIP)||isReachable(newIP)){
                isGetNextIP = false;
            }
        }

        return newIP;
    }
    public static boolean isReachable(String ip){
        boolean result = false;
        if("".equals(ip.trim())) return false;
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(ip);
            result = inetAddress.isReachable(3000);
        } catch (UnknownHostException e) {
            log.error("IP {} is unkownHost.", ip);
        } catch (IOException e) {
            log.error("IP {} is io Error",ip);
        }
        log.info("IP {} is reachable.", ip);
        return result;
    }
}
