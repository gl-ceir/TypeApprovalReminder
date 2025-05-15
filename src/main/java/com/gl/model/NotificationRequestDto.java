package com.gl.model;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
@Data
public class NotificationRequestDto {

    private String channelType;
    private String featureName;
    private String  subFeature;
    private String msgLang;
    private String msisdn;
    private String message;
    private String operatorName;
    private LocalDateTime deliveryDateTime;
    private String featureTxnId;

    public NotificationRequestDto(){}
    public NotificationRequestDto(String channelType, String featureName, String msgLang, String msisdn, String message, String operatorName, LocalDateTime deliveryDateTime,String subFeature,String featureTxnId) {
        this.channelType = channelType;
        this.featureName = featureName;
        this.msgLang = msgLang;
        this.msisdn = msisdn;
        this.message = message;
        this.operatorName = operatorName;
        this.deliveryDateTime = deliveryDateTime;
        this.subFeature = subFeature;
        this.featureTxnId = featureTxnId;

    }
}
