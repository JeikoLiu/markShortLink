package com.jeiko.shortlink_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 短链接聚合应用
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
        "com.jeiko.shortlink_demo.admin",
        "com.jeiko.shortlink_demo.project"
})
@MapperScan(value = {
        "com.jeiko.shortlink_demo.admin.dao.mapper",
        "com.jeiko.shortlink_demo.project.dao.mapper"
})
public class AggregationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AggregationServiceApplication.class, args);
    }
}
