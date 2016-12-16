package com.aplool.mail.utils;

import java.io.*;

/**
 * Created by leokao on 12/16/2016.
 */
public class MailHostListFile {
    String filePath = "";
    String hostIPListFile = "mailHostList.txt";

    public MailHostListFile(String filePath) {
        this.filePath = filePath;
    }

    public String getNewHostIP() {
        String resultIP = "";
        File inputFile = null;
        File tempFile = null;
        try {
            inputFile = new File(filePath + "/" + hostIPListFile);
            tempFile = new File(filePath + "/" + hostIPListFile + ".tmp");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                if (resultIP.equals(""))  {
                    resultIP = trimmedLine;
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            tempFile.renameTo(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (inputFile != null) {
                inputFile.exists();
            }
            if (tempFile != null) {
                tempFile.exists();
            }
            return resultIP;
        }
    }
}
