package com.aplool.mail.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by leokao on 11/18/2016.
 */
public class MailHeaderConfig {
    private static final Logger log = LoggerFactory.getLogger(MailHeaderConfig.class);
    private Properties mHeaderProperties = new Properties();
    public boolean debugFlag = false;

    public MailHeaderConfig(String fileName) {
        InputStream fileInput = null;
        try {
                fileInput = new FileInputStream(fileName);
                mHeaderProperties.load(fileInput);
                if (debugFlag) {
                    Enumeration e = mHeaderProperties.propertyNames();

                    while (e.hasMoreElements()) {
                        String headerKey = (String) e.nextElement();
                        log.debug("{}:{}", headerKey, this.getHeaderProperties().getProperty(headerKey));
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean saveToFile(String fileName) {
        OutputStream fileOutput = null;
        try {
            fileOutput = new FileOutputStream(fileName);
            mHeaderProperties.store(fileOutput,"");
            return true;
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (fileOutput != null) {
                try {
                    fileOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public Properties getHeaderProperties() {
        return mHeaderProperties;
    }

    public void setHeaderProperties(Properties mHeaderProperties) {
        this.mHeaderProperties = mHeaderProperties;
    }

}
