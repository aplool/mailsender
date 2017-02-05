package com.aplool.mail;

import com.aplool.macro.MarcoBuilder;
import com.aplool.macro.MarcoExecutor;
import com.aplool.mail.model.MailAddress;
import com.aplool.mail.model.MailHeaderConfig;
import com.aplool.mail.model.MailHostConfig;
import com.aplool.mail.model.MailItem;
import com.aplool.mail.utils.MailAgent;
import com.aplool.mail.utils.MailAgentManager;
import com.aplool.mail.utils.MailHostListFile;
import com.aplool.mail.utils.MailListManager;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.configuration2.io.InputStreamSupport;
import org.apache.commons.mail.EmailConstants;
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
import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * Created by longtai on 2016/12/1.
 */
public class Main {
    static Logger log = LoggerFactory.getLogger(Main.class);

    MailHostConfig mailHostConfig = null;
    MailHeaderConfig mailHeaderConfig;
    String mailListFilename;
    String messageBody;
    MailAgent mailAgent = null;
    MarcoExecutor executor;
    MailHostListFile mailHostListFile;
    MailAgentManager mailAgentManager;
    MailListManager mailManager;


    public Main(String defaultPath) throws RuntimeException{
        initAppConfig(defaultPath);
        initMarcoExecutor(defaultPath);
        intiMailAgentManager(defaultPath);

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
    private void intiMailAgentManager(String defaultPath) throws RuntimeException{
        mailHostListFile = new MailHostListFile(defaultPath+"mailHostList.txt");
        mailHeaderConfig = new MailHeaderConfig(defaultPath+"mailHeader.config");
        mailAgentManager = new MailAgentManager(this.executor);
        mailAgentManager.setMailHeaders(mailHeaderConfig);
        mailAgentManager.setMailServers(mailHostListFile);
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

    public void start() {
        ExecutorService mailRequestPool = Executors.newFixedThreadPool(App.getConfig().getInt("mailagent.max"));
        ExecutorService pool = Executors.newFixedThreadPool(App.getConfig().getInt("mailagent.max"));
        while (mailAgentManager.isNext()) {
            Future<MailAgent> future = mailRequestPool.submit(mailAgentManager.get());
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    MailAgent mailAgent = null;
                    try {
                        mailAgent = future.get();
                        if (mailAgent != null) {
                            List<String> emails = mailManager.next();
                            emails.add(App.getConfig().getString("seed.email"));
                            log.info("Send Mail with Size {}", emails.size());
                            mailAgent.sendBulk(emails, messageBody);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

            });
        }

        mailRequestPool.shutdown();
        pool.shutdown();
    }

    public static void main(String[] args) {
        log.info("Mail Send Start :");

        String defaultPath = (args.length==0)?System.getProperty("user.dir")+System.getProperty("file.separator"):args[0];
        Main main = null;

        try {
            main = new Main(defaultPath);
            main.start();

        } catch (RuntimeException e) {
            log.error(e.getMessage(),e.getCause());
        }

    }
}
