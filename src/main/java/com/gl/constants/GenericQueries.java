package com.gl.constants;

import static com.gl.constants.ProcessKeys.FeatureName;

public interface GenericQueries {

    String PARAM_START_RANGE = "<START_RANGE>";
    String PARAM_END_RANGE = "<END_RANGE>";
    String PARAM_ID = "<ID>";
    String PARAM_CREATED_ON = "<CREATED_ON>";
    String PARAM_HOSTNAME = "<HOSTNAME>";
    String MODULE_NAME = "<MODULE_NAME>";

    String FEATURE_NAME = "<FEATURE_NAME>";
    String PARAM_STATUS = "<STATUS>";
    String PARAM_STATUS_CODE = "<STATUS_CODE>";
    String PARAM_TIME_TAKEN = "<TIME_TAKEN>";
    String PARAM_ERROR_MESSAGE = "<ERROR_MESSAGE>";
    String TABLE_NAME = "<TABLE_NAME>";
    String PARAM_SUCCESS_COUNT = "<SUCCESS_COUNT>";
    String PARAM_SUCCESS_COUNT2 = "<SUCCESS_COUNT2>";
    String PARAM_FAIL_COUNT = "<PARAM_FAIL_COUNT>";

    String INSERT_MODULE_AUDIT_TRAIL = "insert into aud.modules_audit_trail (status_code, status, error_message, module_name, feature_name, action, count,execution_time,info,server_name) values (201, 'Initial', 'NA', '" + MODULE_NAME + "', '" + FEATURE_NAME + "', 'Insert', 0,0,'starting " + FEATURE_NAME + "','" + PARAM_HOSTNAME + "')";

    String UPDATE_MODULE_AUDIT_TRAIL_ORACLE = "update aud.modules_audit_trail set    status_code=" + PARAM_STATUS_CODE + ", status='" + PARAM_STATUS + "', count='" + PARAM_SUCCESS_COUNT + "',   count2='" + PARAM_SUCCESS_COUNT2 + "',   failure_Count='" + PARAM_FAIL_COUNT + "',       execution_time='" + PARAM_TIME_TAKEN + "', error_message='" + PARAM_ERROR_MESSAGE + "'  , modified_on = CURRENT_TIMESTAMP where  id = " + PARAM_ID;

//    String SELECT_MODULE_AUDIT_TRAIL_ORACLE = "select  created_on, feature_name ,status_code from aud.modules_audit_trail where feature_name='" + FEATURE_NAME + "' and CREATED_ON >= to_date( '" + PARAM_START_RANGE + "', 'YYYY-MM-DD HH24:MI:SS' ) and CREATED_ON < to_date( '" + PARAM_END_RANGE + "', 'YYYY-MM-DD HH24:MI:SS' )";

    String SELECT_MODULE_AUDIT_TRAIL_TILL_NOW = "select  created_on, feature_name ,status_code from aud.modules_audit_trail where  feature_name='" + FEATURE_NAME + "' and module_name='" + MODULE_NAME + "' ";

    String SELECT_NON_TYPE_APPROVAL = "SELECT ai.IMEI, ai.IMSI, ai.MSISDN, ai.MOBILE_OPERATOR FROM " + TABLE_NAME + " ai JOIN MOBILE_DEVICE_REPOSITORY mdr ON ai.tac = mdr.DEVICE_ID WHERE ( ai.msisdn != '' OR ai.msisdn is not null) and   mdr.IS_TYPE_APPROVED != 1    and  ai.MSISDN not in(select msisdn from notification where SUB_FEATURE = '"+FeatureName+"') ";


}
