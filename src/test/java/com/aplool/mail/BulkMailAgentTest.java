package com.aplool.mail;

import com.aplool.mail.utils.MailHostListFile;
import com.google.common.cache.CacheBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

import static org.junit.Assert.*;

/**
 * Created by longtai on 2017/2/8.
 */
public class BulkMailAgentTest {
    Logger log = LoggerFactory.getLogger(this.getClass());

    MailHostListFile servers;
    @Before
    public void setUp() throws Exception {
        servers = new MailHostListFile(getClass().getResource("/mailHostList.txt").getFile());
    }

    @Test
    public void build() throws Exception {
        ExecutorService poolBuilder = Executors.newFixedThreadPool(App.getConfig().getInt("mailagent.max"));
        ExecutorService pool = Executors.newFixedThreadPool(App.getConfig().getInt("mailagent.max"));

        while(servers.isNext()){
            String ip = servers.getNextReachableHost();
//            Callable<BulkMailAgent> agent = BulkMailAgent.build(ip);
//            agent.call();
            Future<BulkMailAgent> future = poolBuilder.submit(BulkMailAgent.build(ip));
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    BulkMailAgent agent = null;
                    try {
                        agent = future.get();
                        if(agent != null) agent.close();
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(),e.getCause());
                    } catch (ExecutionException e) {
                        log.error(e.getMessage(),e.getCause());
                    }

                }
            });
        }
        poolBuilder.shutdown();
        pool.shutdown();
    }

}
