package com.srdcloud.taas.common.infra.event;

import com.srdcloud.taas.common.event.CaseCompileEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CompileEventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(CompileEventProducer.class);

    @Autowired
    private KafkaTemplate<String, CaseCompileEvent> kafkaTemplate;

    @Value("${taas.compiler.producer-topic}")
    private String producerTopic;

    public void writeCompileEventLog(CaseCompileEvent caseCompileEvent) {
        LOG.info("send event {}", caseCompileEvent);
        kafkaTemplate.send(producerTopic, caseCompileEvent);
    }
}
