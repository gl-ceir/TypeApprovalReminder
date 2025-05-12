package com.gl.Processer.service;

import com.gl.Model.OperatorSeries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@Service
@Component
public class Process {
    static Logger log = LogManager.getLogger(Process.class);

    @Autowired
    ModulesAudit modulesAudit;

    public void start(Connection conn, String table) { //mobile_operator, ACTUAL_IMEI
        String str = getLastRunDate(conn);
        var i = modulesAudit.insertModuleAudit(conn, "NonTypeApproval");
        String msg = getEirsMessage(conn);
        getImeiDetailsAndSendNotification(conn, msg, "ACTIVE_IMEI_WITH_DIFFERENT_MSISDN");
        updateLastRunDate(conn);
        //   modulesAudit.updateModuleAudit(conn, tcount, nR, failCount, i);
    }


    public static String getLastRunDate(Connection conn) {
        String date = "";
        String query = "  select  value from sys_param where tag='type_approval_reminder_last_run_date' ";  //????????????
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query);) {
            if (rs.next()) {
                date = rs.getString("value");
            }
            if (date.equalsIgnoreCase(""))
                date = "2000-01-01";
        } catch (Exception e) {
            log.error("  Unable to run [Query] :" + query);
        }
        return date;
    }

    public static String getEirsMessage(Connection conn) {
        String data = "";
        String query = "  select  value from eirs_response_param  where tag='type_approval_reminder_message' and language ='en' "; //????????????
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query);) {
            if (rs.next()) {
                data = rs.getString("value");
            }
        } catch (Exception e) {
            log.error("  Unable to run [Query] :" + query);
        }
        return data;
    }

    public static void getImeiDetailsAndSendNotification(Connection conn, String msg, String table) {
        String query = "SELECT ai.imei, ai.imsi, ai.msisdn ai.mobile_operator FROM " + table + " ai JOIN MOBILE_DEVICE_REPOSITORY mdr ON ai.tac = mdr.DEVICE_ID WHERE ( ai.msisdn != '' OR ai.msisdn is not null) and   mdr.IS_TYPE_APPROVED != 1";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query);) {
            if (rs.next()) {
                sendNotification(rs.getString("imei"), rs.getString("imsi"), rs.getString("misisdn"), rs.getString("mobile_operator"), msg);
            }
        } catch (Exception e) {
            log.error("  Unable to run [Query] :" + query);
        }
    }

    static void sendNotification(String imei, String imsi, String msisdn, String operator, String msg) {


        //callHttpRequest();

    }


    private void updateLastRunDate(Connection conn) {
        String query = "update sys_param set value =CURRENT_TIMESTAMP where tag ='type_approval_reminder_last_run_date' ";  //??????????????????/*/**/*/
        log.info("Query : {} ", query);
        try {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(query);
            }
        } catch (Exception e) {
            log.error(" : Unable to run query: " + e.getLocalizedMessage() + " [Query] :" + query);
        }
    }


//    public ArrayList<OperatorSeries> getOpeartorSeries(Connection conn) {
//        ArrayList<OperatorSeries> ar = new ArrayList();
//        String query = "  select  series_start,  series_end,series_type, operator_name from operator_series";
//        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query);) {
//            if (rs.next()) {
//                ar.add(new OperatorSeries(rs.getInt("series_start"), rs.getInt("series_end"),
//                        rs.getString("series_type"), rs.getString("operator_name"))
//                );
//            }
//        } catch (Exception e) {
//            log.error("  Unable to run [Query] :" + query);
//        }
//        return ar;
//    }

}
