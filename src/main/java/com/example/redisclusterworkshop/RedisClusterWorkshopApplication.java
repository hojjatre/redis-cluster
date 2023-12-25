package com.example.redisclusterworkshop;

import com.example.redisclusterworkshop.component.RateLimitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RedisClusterWorkshopApplication {


    public static void main(String[] args) {
        SpringApplication.run(RedisClusterWorkshopApplication.class, args);
    }

}
