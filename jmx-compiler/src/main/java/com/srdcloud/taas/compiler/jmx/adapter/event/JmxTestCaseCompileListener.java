package com.srdcloud.taas.compiler.jmx.adapter.event;

import com.srdcloud.taas.common.adpater.event.EventListener;
import com.srdcloud.taas.common.event.TestCaseCompileNotify;
import com.srdcloud.taas.compiler.jmx.application.JmxCompileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JmxTestCaseCompileListener implements EventListener {
    @Autowired
    private JmxCompileService jmxCompileService;

    public void onHandleEvent(TestCaseCompileNotify testCaseCompileNotify) {
        if ("jmeter".equals(testCaseCompileNotify.getTarget())) {
            jmxCompileService.compileJmx(testCaseCompileNotify.getCaseId(), testCaseCompileNotify.getRecordId());
        }
    }

}
