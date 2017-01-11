package com.aplool.mail;

import org.junit.Test;

import java.util.Properties;

/**
 * Created by longtai on 2017/1/11.
 */
public class AppTest {

    @Test
    public void testDefualtValue() throws Exception{
        System.out.println(App.getConfig().getInt("mailagent.max"));
        Properties props = new Properties();
        props.load(AppTest.class.getClassLoader().getResourceAsStream("app.config"));
        App.addConfig(props);
        System.out.println(App.getConfig().getInt("mailagent.max"));
    }
}
