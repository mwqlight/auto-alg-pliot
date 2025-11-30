package com.algcockpit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 算法驾驶舱平台启动类
 * 
 * @author Algorithm Cockpit Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
@MapperScan("com.algcockpit.repository")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}