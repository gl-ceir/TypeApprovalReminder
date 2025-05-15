package com.gl;

import com.gl.service.TypeApprovalProcess;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.TimeUnit;

@EnableJpaRepositories({"com.gl.persistence.repository"})
@EntityScan({"com.gl.persistence.entities"})
@SpringBootConfiguration
@SpringBootApplication(scanBasePackages = {"com.gl"})
@EnableEncryptableProperties
public class Application implements CommandLineRunner {
    @Autowired
    ApplicationContext applicationContext;
    static Logger log = LogManager.getLogger(Application.class);

    public static void main(String... args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            applicationContext.getBean(TypeApprovalProcess.class).process();
        } catch (Exception e) {
            log.error("Error while processing Error:{}", e.getMessage(), e);
        }
        try {
            TimeUnit.SECONDS.sleep(1);
            System.exit(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}