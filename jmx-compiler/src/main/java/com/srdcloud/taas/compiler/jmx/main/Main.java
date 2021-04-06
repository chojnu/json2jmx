package com.srdcloud.taas.compiler.jmx.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.srdcloud.taas.compiler.jmx", "com.srdcloud.taas.common"})
@RestController
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/actuator/health")
    public String healthCheck(){
        return "OK";
    }
}
