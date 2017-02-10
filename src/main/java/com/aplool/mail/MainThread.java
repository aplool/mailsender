package com.aplool.mail;

import com.aplool.macro.MarcoBuilder;
import com.aplool.macro.MarcoExecutor;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.utils.MailAgentManager;
import com.aplool.mail.utils.MailHostListFile;
import com.aplool.mail.utils.MailListManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by longtai on 2017/2/6.
 */
public class MainThread extends Thread {
    Logger log = LoggerFactory.getLogger(this.getClass());

    MailHostConfig mailHostConfig = null;
    MailHeaderConfig mailHeaderConfig;
    String mailListFilename;
    String messageBody;
    MarcoExecutor executor;
    MailHostListFile servers;
    MailAgentManager mailAgentManager;
    MailListManager mailManager;

    String defaultPath;

    public MainThread(String defaultPath) throws RuntimeException{
        this.defaultPath = defaultPath;
        initAppConfig(defaultPath);
        initMarcoExecutor(defaultPath);
        initMailHostServer(defaultPath);
        mailListFilename = new File(defaultPath + "mailToList.txt").getAbsolutePath();
        initMailListManager(defaultPath);

        initMessageBody(defaultPath + "mailBody.txt");

    }

    private void initAppConfig(String defaultPath){
        Properties props = new Properties();
        InputStream stream = null;
        try {
            stream = new FileInputStream(defaultPath+"app.config");
            props.load(stream);
            App.addConfig(props);
        } catch (IOException e) {
            log.error("Initial Custom Config Error : {} ", e.getMessage());
        } finally {
            if(stream != null ){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void initMailHostServer(String defaultPath) throws RuntimeException{
        servers = new MailHostListFile(defaultPath+"mailHostList.txt");
    }
    private void intiMailAgentManager(String defaultPath) throws RuntimeException{
        servers = new MailHostListFile(defaultPath+"mailHostList.txt");
        mailHeaderConfig = new MailHeaderConfig(defaultPath+"mailHeader.config");
        mailAgentManager = new MailAgentManager(this.executor);
        mailAgentManager.setMailHeaders(mailHeaderConfig);
        mailAgentManager.setMailServers(servers);
    }
    private void initMailListManager(String defaultPath) throws RuntimeException{
        mailManager = new MailListManager(mailListFilename,App.getConfig().getInt("seed.qty"));
    }
    private void initMessageBody(String filename) throws RuntimeException{
        try {
            byte[] content = Files.readAllBytes(Paths.get(filename));
            messageBody = new String(content);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Message Body init error with : %s", filename));
        }
    }


    private void initMarcoExecutor(String defaultPath) throws RuntimeException{
        this.executor = new MarcoExecutor();
        try {
            MarcoBuilder.build(this.executor,Paths.get(defaultPath+"marco"));
        } catch (Exception e) {
            throw new RuntimeException("MarcoExecutor add Extend Marcos Error.", e);
        }
    }

    private MarcoExecutor createMarcoExecutor(){
        MarcoExecutor executor = new MarcoExecutor();
        try {
            MarcoBuilder.build(executor,Paths.get(this.defaultPath+"marco"));
        } catch (Exception e) {
            log.error("MarcoExecutor add Extend Marcos Error.", e);
        }
        return executor;
    }
    @Override
    public void run() {
        ExecutorService poolBuilder = Executors.newFixedThreadPool(App.getConfig().getInt("mailagent.max"));
        ExecutorService pool = Executors.newFixedThreadPool(App.getConfig().getInt("mailagent.max"));
        while(servers.isNext()){
            String ip = servers.getNextReachableHost();

            Future<BulkMailAgent> future = poolBuilder.submit(BulkMailAgent.build(ip, createMarcoExecutor()));
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    BulkMailAgent agent = null;
                    try {
                        agent = future.get();
                        if(agent != null) {
                            List<String> mails = mailManager.next();
                            mails.add(App.getConfig().getString("seed.email"));
                            agent.send(mails, messageBody);
                        }
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
