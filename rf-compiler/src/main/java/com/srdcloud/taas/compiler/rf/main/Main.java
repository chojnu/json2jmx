package com.srdcloud.taas.compiler.rf.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.srdcloud.taas.compiler.rf"})
public class Main {
    public static void main(String[] args)
    {
        SpringApplication.run(Main.class, args);
    }
}
