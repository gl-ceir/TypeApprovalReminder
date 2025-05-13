package com.gl.config;

import com.gl.model.NotificationRequestDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Component
public class ApiHttpConnection {

    private final Logger log = LogManager.getLogger(this.getClass());

    private RestTemplate restTemplate = null;

    @Value("${notification.url}")
    String url;

    public boolean sendNotification(NotificationRequestDto notificationDto) {
        long start = System.currentTimeMillis();
        try {
            SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
            clientHttpRequestFactory.setConnectTimeout(10000);
            clientHttpRequestFactory.setReadTimeout(10000);
            restTemplate = new RestTemplate(clientHttpRequestFactory);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<NotificationRequestDto> request = new HttpEntity<NotificationRequestDto>(notificationDto, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
            log.info(" Sent Request:{}, TimeTaken:{} Response:{}", notificationDto, responseEntity,
                    (System.currentTimeMillis() - start));
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info(" Sent Successfully Request:{}", notificationDto);
                return true;
            }
        } catch (org.springframework.web.client.ResourceAccessException resourceAccessException) {
            log.error("Error while Sending   resourceAccessException:{} Request:{}",
                    resourceAccessException.getMessage(), notificationDto, resourceAccessException);
        } catch (Exception e) {
            log.error("Error while Sending  :{} Request:{}", e.getMessage(), notificationDto, e);
        }
        return false;
    }


}
