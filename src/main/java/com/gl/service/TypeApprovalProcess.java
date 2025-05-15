package com.gl.service;


import com.gl.persistence.entities.ModuleAuditTrail;
import com.gl.service.serviceImpl.ModuleAuditTrailService;
import com.gl.service.serviceImpl.TypeApprovalServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.gl.constants.ProcessKeys.FeatureName;
import static com.gl.constants.ProcessKeys.moduleName;

@Service
public class TypeApprovalProcess {
    @Autowired
    TypeApprovalServiceImpl taservice;
    @Autowired
    ModuleAuditTrailService moduleAuditTrailService;
    static Logger log = LogManager.getLogger(TypeApprovalProcess.class);
    public static int successCount = 0;
    public static int failCount = 0;
    public static Long auditKey;

    public void process() {
        LocalDate localDate = LocalDate.now();
        if (!moduleAuditTrailService.canProcessRun(localDate, moduleName, FeatureName)) {
            log.info("Process:{} will not execute it may already Running or Completed for the day {}", "TypeApprovalReminder", localDate);
            return;
        }
        log.info("Process:{} will start", "TypeApprovalReminder");
        Long start = System.currentTimeMillis();
        auditKey = moduleAuditTrailService.createAudit(ModuleAuditTrail.builder().createdOn(LocalDateTime.of(localDate, LocalTime.now())).moduleName(moduleName).featureName(FeatureName).build());
        ModuleAuditTrail updateModuleAuditTrail = ModuleAuditTrail.builder().id(auditKey).moduleName(moduleName).featureName(FeatureName).build();
        try {
            taservice.process(0);
            updateModuleAuditTrail.setStatusCode(200);
        } catch (Exception e) {
            log.error("Error while Process Error:{} ", e.getMessage(), e);
            updateModuleAuditTrail.setStatusCode(500);
            updateModuleAuditTrail.setErrorMessage(e.getMessage());
        }
        updateModuleAuditTrail.setTimeTaken(System.currentTimeMillis() - start);
        updateModuleAuditTrail.setCount(successCount + failCount);
        updateModuleAuditTrail.setCount1(successCount + failCount);
        updateModuleAuditTrail.setFailureCount(failCount);
        moduleAuditTrailService.updateAudit(updateModuleAuditTrail);
    }


}
