package com.srdcloud.taas.compiler.jmx.infra.persist;

import com.srdcloud.taas.common.infra.nosqlpersist.TestCaseLanguageRepository;
import com.srdcloud.taas.common.infra.nosqlpersist.TestCaseLanguagePo;
import com.srdcloud.taas.common.infra.persist.TestCasePo;
import com.srdcloud.taas.common.infra.persist.TestCaseRepository;
import com.srdcloud.taas.compiler.jmx.domain.JmxTestCase;
import com.srdcloud.taas.compiler.jmx.domain.JmxTestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JmxTestCaseRepoImpl implements JmxTestCaseRepository {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private TestCaseLanguageRepository testCaseLanguageRepository;

    @Override
    public JmxTestCase findById(long id) {
        TestCasePo testCasePo = testCaseRepository.findById(id);
        TestCaseLanguagePo testCaseLanguagePo = testCaseLanguageRepository.findByCaseId(id);
        return toJmxTestCase(testCasePo, testCaseLanguagePo);
    }

    private JmxTestCase toJmxTestCase(TestCasePo testCasePo, TestCaseLanguagePo testCaseLanguagePo) {
        if (testCasePo == null || testCaseLanguagePo == null) {
            return null;
        }
        JmxTestCase jmxTestCase = new JmxTestCase();
        jmxTestCase.setId(testCasePo.getId());
        jmxTestCase.setStatus(testCasePo.getStatus());
        jmxTestCase.setTestCaseJson(testCaseLanguagePo.getTestCaseJson());
        return jmxTestCase;
    }

    @Override
    public void save(JmxTestCase jmxTestCase) {
        testCaseRepository.save(toTestCase(jmxTestCase));
    }

    private TestCasePo toTestCase(JmxTestCase jmxTestCase) {
        TestCasePo testCasePo = new TestCasePo();
        testCasePo.setId(jmxTestCase.getId());
        testCasePo.setStatus(jmxTestCase.getStatus());
        return testCasePo;
    }
}
