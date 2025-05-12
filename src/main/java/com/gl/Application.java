package com.gl;

import com.gl.Processer.MainController;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootConfiguration
@SpringBootApplication(scanBasePackages = {"com.gl"})
@EnableEncryptableProperties
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        MainController controller = ctx.getBean(MainController.class);
        controller.startService(ctx);
        System.exit(0);
    }
}