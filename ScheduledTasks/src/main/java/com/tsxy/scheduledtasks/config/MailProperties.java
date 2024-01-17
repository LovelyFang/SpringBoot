package com.tsxy.scheduledtasks.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @Author Liu_df
 * @Date 2024/1/11 10:12
 */

@Validated
@Component
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private String domain;
    private String from;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


}
