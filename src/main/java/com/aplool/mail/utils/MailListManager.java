package com.aplool.mail.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by longtai on 2017/2/2.
 */
public class MailListManager {
    static String ERROR_MESSAGE_MAIL_LIST_EMPTY = "MailList is empty. [%s]";
    static String ERROR_MESSAGE_MAIL_LIST_IO = "MailSList is initial error . [%s]";

    int seedQty;
    int index;
    int emailSize;

    String mailFileName;
    List<String> emails;
    public MailListManager(String mailFileName, int seedQty){
        this.mailFileName = mailFileName;
        this.seedQty = seedQty;
        init();
    }

    public void init() throws RuntimeException{
        Stream<String> stream = null;
        try {
            stream = Files.lines(Paths.get(mailFileName));
            emails = stream.filter(line->!line.isEmpty())
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(String.format(ERROR_MESSAGE_MAIL_LIST_IO,mailFileName),e.getCause());
        }
        if(emails.size()==0) throw new RuntimeException(String.format(ERROR_MESSAGE_MAIL_LIST_EMPTY,mailFileName));
        if((emails.size()==1)&&(seedQty >1)){
            int addQty = seedQty - emails.size();
            for(int i=0;i<addQty;i++) {
                emails.add(emails.get(0));
            }
        }
        this.emailSize = emails.size();
        this.index =0;
    }

    public boolean isNext(){
        if(emailSize > index) return true;
        return false;
    }
    public synchronized  List<String> next(){
        return next(false);
    }
    public synchronized List<String> next(boolean isCycleFetch){
        if(!isNext() && !isCycleFetch) return new ArrayList<String>();
        int nextIndex = Math.min(index+seedQty,emailSize);
        List<String> result =  emails.subList(index, nextIndex);

        if(isCycleFetch && nextIndex==emailSize){
            index = 0;
            nextIndex = seedQty - result.size();
            result.addAll(emails.subList(index,nextIndex));
        }

        index = nextIndex;
        return result;
    }
}
