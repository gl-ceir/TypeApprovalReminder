package com.gl.service.serviceImpl;

import com.gl.config.ApiHttpConnection;
import com.gl.model.NotificationRequestDto;
import com.gl.persistence.repository.EirsResponseParamRepository;
import com.gl.persistence.repository.SysParamRepository;
import com.gl.processer.services.QueryExecutorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.gl.constants.GenericQueries.SELECT_NON_TYPE_APPROVAL;
import static com.gl.constants.GenericQueries.TABLE_NAME;
import static com.gl.constants.ProcessKeys.*;
import static com.gl.service.TypeApprovalProcess.*;


@Service
public class TypeApprovalServiceImpl {
    static Logger log = LogManager.getLogger(TypeApprovalServiceImpl.class);

    @Autowired
    EirsResponseParamRepository eirsResponseParamRepository;

    @Autowired
    SysParamRepository sysParamRepository;

    @Autowired
    QueryExecutorService queryExecutorService;

    @Autowired
    ApiHttpConnection apiHttp;

    public void process(int i) {
        String message = eirsResponseParamRepository.findByTagAndLanguage(type_approval_reminder_message, defaultLanguage).getValue();
        var time = getDeliveryTime();
        List.of(active_uni_table, active_uni_diff_msisdn_table).stream().forEach(table -> getImeiDetailsAndSendNotification(message, table, time));
        log.info("No of Success Count {} and Fail Count {}", successCount, failCount);
    }

    public void getImeiDetailsAndSendNotification(String msg, String table, LocalDateTime time) {
        String query = SELECT_NON_TYPE_APPROVAL.replaceAll(TABLE_NAME, table);
        log.info(" Query for {} ", query);
        try (Connection connection = queryExecutorService.getJdbcTemplate().getDataSource().getConnection();
             Statement st = connection.createStatement();
             ResultSet resultSet = st.executeQuery(query);) {
            log.info("No of row:{} query:[{}]", resultSet.getRow(), query);
            while (resultSet.next()) {
                NotificationRequestDto notificationRequestDto
                        = new NotificationRequestDto(channelType, moduleName, defaultLanguage, resultSet.getString("MSISDN"), msg,
                        resultSet.getString("MOBILE_OPERATOR"), time, FeatureName, auditKey.toString());
                sendNotification(notificationRequestDto);
            }
        } catch (SQLException e) {
            log.error("Error while getting IMEI Details and Sending Notification Error:{} Query:[{}]", e.getMessage(), query, e);
        }
    }

    private void sendNotification(NotificationRequestDto notificationRequestDto) {
        var counter = apiHttp.sendNotification(notificationRequestDto) ? successCount++ : failCount++;
    }


    private LocalDateTime getDeliveryTime() {
        String startTimeStr = sysParamRepository.findByTag(sms_start_time).getValue();
        String endTimeStr = sysParamRepository.findByTag(sms_end_time).getValue();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
        LocalTime startTime = LocalTime.parse(startTimeStr, timeFormatter);
        LocalTime endTime = LocalTime.parse(endTimeStr, timeFormatter);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now(), startTime);
        LocalDateTime endDate = LocalDateTime.of(LocalDate.now(), endTime);
        if (now.isBefore(startDate)) {
            return startDate;
        }
        if (now.isAfter(endDate)) {
            return startDate.plusDays(1);
        }
        return LocalDateTime.now();
    }

}

