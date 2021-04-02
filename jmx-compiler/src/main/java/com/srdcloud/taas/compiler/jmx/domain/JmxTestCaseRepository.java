package com.srdcloud.taas.compiler.jmx.domain;

public interface JmxTestCaseRepository {
    JmxTestCase findById(long id);

    void save(JmxTestCase jmxTestCase);
}
