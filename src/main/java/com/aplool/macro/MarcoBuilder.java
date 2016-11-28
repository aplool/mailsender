package com.aplool.macro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by longtai on 2016/11/24.
 *
 */
public class MarcoBuilder {
    static Logger log = LoggerFactory.getLogger(MarcoBuilder.class);

    static String DEFAULT_MARCOS_PATH = "/default-marcos";

    static public void build(MarcoExecutor executor, Path dir){
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file: stream) {
                String marcoName = file.getFileName().toString();
                List<String> expressions = Files.lines(file, Charset.defaultCharset())
                        .filter(line -> !line.isEmpty())
                        .collect(Collectors.toList());
                RandomList marco = new RandomList(executor);
                marco.setExpressions(expressions);
                executor.addMarco(marcoName, marco);
            }
        } catch (IOException | DirectoryIteratorException x) {
            log.error("File Iterator",x);
        }
    }
}
