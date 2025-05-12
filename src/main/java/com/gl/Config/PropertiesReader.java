package com.gl.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources({
        @PropertySource(value = {"file:application.properties"}, ignoreResourceNotFound = true),
        @PropertySource(value = {"file:configuration.properties"}, ignoreResourceNotFound = true)
})
public class PropertiesReader {

    @Value("${appdbName}")
    public String appdbName;

}