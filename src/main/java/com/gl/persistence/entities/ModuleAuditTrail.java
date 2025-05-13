package com.gl.persistence.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// s@Table(name = "module_audit_trail", catalog = "app")

public class ModuleAuditTrail {
    private Long id;
    private LocalDateTime createdOn;
    private Date createdDate;
    private Integer statusCode;
    private String featureName;
    private String moduleName;
    private Integer count, count1, failureCount;
    private Long timeTaken;
    private String errorMessage;
}
