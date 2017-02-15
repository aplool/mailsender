package com.aplool.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longtai on 2017/2/15.
 */
public class RandomFileReader {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private File fileInput;
    private RandomAccessFile accessFile;
    private BufferedReader reader;
    private InputStream is;
    private InputStreamReader isr;

    private long lineIndex = 0;
    private final int bufferSize = 8192;

    public RandomFileReader(final File file) throws RuntimeException{
        if(file.exists() == false) {
            throw new RuntimeException("File is not exist. : " + file.getAbsolutePath());
        }
        this.fileInput = file;
        try {
            this.accessFile = new RandomAccessFile(fileInput, "r");
            is = Channels.newInputStream(accessFile.getChannel());
            isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);

        } catch (FileNotFoundException e) {
            log.debug("FileNoteFoundException with RandomFileReader constractor : " + fileInput.getAbsolutePath());
            //e.printStackTrace();
        }
    }

    public String getNextLine() throws Exception{
        lineIndex++;
        return reader.readLine();
    }

    public List<String> getNextLines(int qty) throws Exception{
        ArrayList<String> result = new ArrayList<String>();
        for(int i= 0;i<qty;i++){
            String line = getNextLine();
            if(line == null) break;
            result.add(line);
        }
        return result;
    }

    public long getLineIndex(){
        return lineIndex;
    }

    public void seek(final long position) throws IOException {
        if(position < 0){
            return;
        }
        if(position > fileInput.length()){
            return;
        }
        accessFile.seek(position);
        reader = new BufferedReader(isr, bufferSize);
    }

    public synchronized long getOffset() throws IOException {
        return accessFile.getChannel().position()-bufferSize;
    }
    public void close(){
        try {
            reader.close();
            accessFile.close();
        } catch (IOException e) {
            log.error(e.getMessage(),e.getCause());
        }
    }
}
