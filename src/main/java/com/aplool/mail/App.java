package com.aplool.mail;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by longtai on 2017/1/11.
 */
public final class App {
    static Logger log = LoggerFactory.getLogger(App.class);
    static {
        initConfig();
    }
    private App(){

    }
    private static Configuration config = null;
    private static void initConfig(){
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
            new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(params.properties()
                    .setFileName(App.class.getClassLoader().getResource("app-default.config").getPath()));

        try {
            config = builder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new RuntimeException("Initial App Config Error with " + e.getMessage());
        }

    }

    public static Configuration getConfig(){
        if(config == null) {
            synchronized (App.class) {
                initConfig();
            }
        }
        return config;
    }
    public static void addConfig(Properties props){
        getConfig();
        for(String key : props.stringPropertyNames()){
            String value = props.getProperty(key);
            if("".equals(value)) break;

            config.setProperty(key,value);
        }
    }

}
