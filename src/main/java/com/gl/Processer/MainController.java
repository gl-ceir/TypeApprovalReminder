package com.gl.Processer;


import com.gl.Config.ConnectionConfiguration;
import com.gl.Processer.service.Process;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service
public class MainController {
    @Autowired
    Process process;
    @Autowired
    ApplicationContext context;

    static Logger log = LogManager.getLogger(MainController.class);

    public void startService(ApplicationContext ctx) {

        var c = (ConnectionConfiguration) context.getBean("connectionConfiguration");
        try (Connection conn = c.getConnection()) {
            process.start(conn, "");
        } catch (Exception e) {
            log.error("Not able to start service {} Though conn {} ", e, c.getConnection().toString());
        }
    }
}
