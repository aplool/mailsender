package com.aplool.mail.utils;

import com.mifmif.common.regex.Generex;

/**
 * Created by longtai on 2016/11/21.
 */
public class RegularGenerator {
    public String generate(String pattern){
        Generex generex = new Generex(pattern);
        return generex.random();
    }
}
