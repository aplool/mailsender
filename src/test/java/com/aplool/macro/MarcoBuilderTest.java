package com.aplool.macro;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by longtai on 2016/11/24.
 */
public class MarcoBuilderTest {
    Logger log = LoggerFactory.getLogger(MarcoBuilderTest.class);

    MarcoExecutor executor;
    @Before
    public void setUp() throws Exception {
        executor = new MarcoExecutor();
    }


    static String[] TEST_MARCO_NAMES = new String[]{
            "%RND_LC_CHAR",
            "%RND_UC_CHAR",
            "%RND_DIGIT",
            "%RND_CHAR",
            "%RND_DIGIT[10-20]",
            "%RND_NUMBER[0-255]",
            "%RND_IP",
            "%FIRST_NAME",
            "%RND_FROM_DOMAIN",
            "%FROM_EMAIL",
    };
    @Test
    public void build() throws Exception {
        MarcoBuilder.build(executor, Paths.get(getClass().getResource("/marco").toURI()));
        Arrays.stream(TEST_MARCO_NAMES).forEach(marco ->
                log.info("Marco : {} -> {}",marco,executor.executeMarco(marco))
        );
    }

}