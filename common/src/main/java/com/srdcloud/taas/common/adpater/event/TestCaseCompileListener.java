package com.srdcloud.taas.common.adpater.event;

import com.srdcloud.taas.common.event.TestCaseCompileNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TestCaseCompileListener {
    @Autowired
    private EventListener eventListener;

    @KafkaListener(topics = "${taas.compiler.listener-topic}")
    public void receive(TestCaseCompileNotify testCaseCompileNotify) {
        eventListener.onHandleEvent(testCaseCompileNotify);
    }
}
