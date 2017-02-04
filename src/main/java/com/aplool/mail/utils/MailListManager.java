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
    int seedQty;
    int index;
    int emailSize;

    String mailFileName;
    List<String> emails;
    public MailListManager(String mailFileName, int seedQty) throws IOException {
        this.mailFileName = mailFileName;
        this.seedQty = seedQty;
        init();
    }

    public void init() throws IOException {
        Stream<String> stream = Files.lines(Paths.get(mailFileName));
        emails = stream.filter(line->!line.isEmpty()).collect(Collectors.toList());
        this.emailSize = emails.size();
        this.index =0;
    }
    public boolean isNext(){
        if(emailSize > index) return true;
        return false;
    }
    public List<String> next(){
        if(!isNext()) return new ArrayList<String>();
        int nextIndex = Math.min(index+seedQty,emailSize);
        List<String> result =  emails.subList(index, nextIndex);
        index = nextIndex;
        return result;
    }
}
