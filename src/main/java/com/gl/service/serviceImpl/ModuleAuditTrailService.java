package com.gl.service.serviceImpl;

import com.gl.constants.DateFormatterConstants;
import com.gl.persistence.entities.ModuleAuditTrail;
import com.gl.processer.services.QueryExecutorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import static com.gl.constants.GenericQueries.*;

@Service
public class ModuleAuditTrailService {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    QueryExecutorService queryExecutorService;

    public Boolean canProcessRun(LocalDate localDate, String moduleName, String featureName) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));

        Boolean startProcess = true;
        String query = SELECT_MODULE_AUDIT_TRAIL_TILL_NOW;
        query = query.replaceAll(MODULE_NAME, moduleName);
        query = query.replaceAll(FEATURE_NAME, featureName);
        query = query.replaceAll(PARAM_START_RANGE, localDateTime.format(DateFormatterConstants.simpleDateFormat));
        // query = query.replaceAll(PARAM_END_RANGE, localDateTime.plusDays(1).format(DateFormatterConstants.simpleDateFormat));
        query = query.replaceAll(PARAM_END_RANGE, LocalDateTime.now().format(DateFormatterConstants.simpleDateFormat));
        log.info("Running Query:{} ", query);
        try {
            List<ModuleAuditTrail> trails = jdbcTemplate.query(query, new RowMapper<ModuleAuditTrail>() {
                @Override
                public ModuleAuditTrail mapRow(ResultSet rs, int rowNum) throws SQLException {
                    ModuleAuditTrail moduleAuditTrail = new ModuleAuditTrail();
                    moduleAuditTrail.setCreatedOn(LocalDateTime.ofInstant(rs.getTimestamp("created_on").toInstant()
                            , ZoneId.systemDefault()));
                    moduleAuditTrail.setStatusCode(rs.getInt("status_code"));
                    moduleAuditTrail.setFeatureName(rs.getString("feature_name"));
                    return moduleAuditTrail;
                }
            });
            long count = trails.stream().filter(trail -> (trail.getStatusCode().intValue() == 200)).count();
            startProcess = (count == 0);
        } catch (Exception e) {
            log.error("Error:{} while running query:[{}]", e.getMessage(), query, e);
        }
        return startProcess;
    }


    public Long createAudit(ModuleAuditTrail moduleAuditTrail) {
        Long key = 0L;
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            key = queryExecutorService.executeAndGetKey(INSERT_MODULE_AUDIT_TRAIL.replaceAll(PARAM_HOSTNAME, localhost.getHostName())
                    .replaceAll(MODULE_NAME, moduleAuditTrail.getModuleName())
                    .replaceAll(FEATURE_NAME, moduleAuditTrail.getFeatureName())
                    .replaceAll(PARAM_CREATED_ON, moduleAuditTrail.getCreatedOn().format(DateFormatterConstants.simpleDateFormat)));
            log.info("Inserted ModuleAuditTrail:{}", moduleAuditTrail + " with key:" + key);
        } catch (Exception ex) {
            log.error("Error to get executing insert moduleAuditTrail:{} Error:{}", moduleAuditTrail, ex.getMessage());
        }
        return key;
    }


    public void updateAudit(ModuleAuditTrail moduleAuditTrail) {
        String errorMessage = moduleAuditTrail.getErrorMessage() == null ? "NA" : moduleAuditTrail.getErrorMessage().replaceAll("'", "");
        String updateAuditQuery = UPDATE_MODULE_AUDIT_TRAIL_ORACLE.replaceAll(PARAM_TIME_TAKEN, String.valueOf(moduleAuditTrail.getTimeTaken()))
                .replaceAll(PARAM_ERROR_MESSAGE, errorMessage)
                .replaceAll(PARAM_STATUS, moduleAuditTrail.getStatusCode() == 200 ? "Success" : "Fail")
                .replaceAll(PARAM_SUCCESS_COUNT, String.valueOf(moduleAuditTrail.getCount()))
                .replaceAll(PARAM_STATUS_CODE, String.valueOf(moduleAuditTrail.getStatusCode()))
                .replaceAll(PARAM_SUCCESS_COUNT2, String.valueOf(moduleAuditTrail.getCount1()))
                .replaceAll(PARAM_FAIL_COUNT, String.valueOf(moduleAuditTrail.getFailureCount()))
                .replaceAll(PARAM_ID, String.valueOf(moduleAuditTrail.getId()));
        queryExecutorService.execute(updateAuditQuery);
    }
}
