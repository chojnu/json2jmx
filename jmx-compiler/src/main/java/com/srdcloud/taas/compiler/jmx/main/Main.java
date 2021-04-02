package com.srdcloud.taas.compiler.jmx.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.srdcloud.taas.compiler.jmx", "com.srdcloud.taas.common"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
