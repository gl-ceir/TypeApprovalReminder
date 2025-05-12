package com.gl.Processer.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Service
public class QueryExecuter {
    static Logger log = LogManager.getLogger(QueryExecuter.class);

    public static int runQuery(Connection conn, String query) {
        log.info("Query : {} ", query);
        var a = 0;
        try {
            try (Statement stmt = conn.createStatement()) {
                a = stmt.executeUpdate(query);
                log.info("Rows Affected :  {}", a);
            }
        } catch (Exception e) {
            log.error(" : Unable to run query: " + e.getLocalizedMessage() + " [Query] :" + query);
        }
        return a;
    }

    public static String getLastRunDate(Connection conn) {
        String date = "2000-01-01";
        String query = "  select  IFNULL(value, '" + date + "') as value from sys_param where tag='trc_nwl_typeapprove_last_run_time'";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query);) {
            if (rs.next()) {
                date = rs.getString("value");
            }
        } catch (Exception e) {
            log.error("  Unable to run [Query] :" + query);
        }
        return date;
    }

}