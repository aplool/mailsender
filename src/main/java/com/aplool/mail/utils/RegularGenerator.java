package com.aplool.mail.utils;

import com.mifmif.common.regex.Generex;

/**
 * Created by longtai on 2016/11/21.
 */
public class RegularGenerator {

    Generex mGenerator ;

    public RegularGenerator(String pattern){
        mGenerator = new Generex(pattern);
    }

    public String generate() {
        return mGenerator.random();
    }
}
