package com.srdcloud.taas.compiler.jmx.application;

import com.srdcloud.taas.common.event.CaseCompileEvent;
import com.srdcloud.taas.common.infra.event.CompileEventProducer;
import com.srdcloud.taas.compiler.jmx.domain.JmxTestCase;
import com.srdcloud.taas.compiler.jmx.domain.JmxTestCaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class JmxCompileService {
    private static final Logger LOG = LoggerFactory.getLogger(JmxCompileService.class);
    @Autowired
    private JmxTestCaseRepository jmxTestCaseRepository;
    @Autowired
    private CompileEventProducer compileEventProducer;

    public void compileJmx(long caseId, String recordId) {
        JmxTestCase jmxTestCase = jmxTestCaseRepository.findById(caseId);
        if (jmxTestCase == null) {
            LOG.warn("Could not find testcase");
            return;
        }
        LOG.info("compile testcase {}", jmxTestCase.toString());
        CaseCompileEvent caseCompileEvent = new CaseCompileEvent(caseId, recordId);
        compileEventProducer.writeCompileEventLog(caseCompileEvent.setToCompiling());
        if (jmxTestCase.compile()) {
            compileEventProducer.writeCompileEventLog(caseCompileEvent.setToFinished());
        } else {
            compileEventProducer.writeCompileEventLog(caseCompileEvent.setToFailed(jmxTestCase.getReason()));
        }
        jmxTestCaseRepository.save(jmxTestCase);
    }
}
