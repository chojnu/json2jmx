package com.srdcloud.taas.common.conf;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.srdcloud.taas.common.infra.persist")
@EntityScan("com.srdcloud.taas.common.infra")
@EnableMongoRepositories(basePackages = "com.srdcloud.taas.common.infra.nosqlpersist")
public class TaasCommonConf {
}
