package com.tsxy.scheduledtasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
@ImportResource(locations={"classpath:spring/spring-job.xml"})
public class ScheduledTasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduledTasksApplication.class, args);
    }

}
