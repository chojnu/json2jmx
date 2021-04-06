package com.srdcloud.taas.common.adpater.event;

import com.srdcloud.taas.common.event.TestCaseCompileNotify;
import com.srdcloud.taas.common.infra.event.CompileEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TestCaseCompileListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestCaseCompileListener.class);

    @Autowired
    private EventListener eventListener;

    @KafkaListener(topics = "${taas.compiler.listener-topic}")
    public void receive(TestCaseCompileNotify testCaseCompileNotify) {
        LOG.info("received notify {}", testCaseCompileNotify);
        eventListener.onHandleEvent(testCaseCompileNotify);
    }
}
