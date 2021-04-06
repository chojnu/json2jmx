package com.srdcloud.taas.compiler.jmx.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srdcloud.taas.common.domain.compile.StepDeser;
import com.srdcloud.taas.common.domain.compile.entity.Testsuite;
import com.srdcloud.taas.compiler.jmx.application.JmxVisitor;
import com.srdcloud.taas.compiler.jmx.infra.jmeterutil.JmxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class JmxTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(JmxTestCase.class);
    private static String JMX_PATH_TMP = "/var/taas/product_ver_%s/testcases/%s/jmeter/case_%s.jmx";
    private long id;
    private long productVId;
    private String status;
    private String testCaseJson;
    private String reason;

    public boolean compile() {
        try {
            ObjectMapper om = new ObjectMapper();
            Testsuite ts = om.readValue(testCaseJson, Testsuite.class);
            JmxVisitor jv = new JmxVisitor();
            ts.accept(jv);
            JmxUtils.save(jv.getHashTree(), String.format(JMX_PATH_TMP, productVId, id, id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
            reason = e.getMessage();
            status = "compilefail";
            return false;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductVId() {
        return productVId;
    }

    public void setProductVId(long productVId) {
        this.productVId = productVId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTestCaseJson() {
        return testCaseJson;
    }

    public void setTestCaseJson(String testCaseJson) {
        this.testCaseJson = testCaseJson;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "JmxTestCase{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", testCaseJson='" + testCaseJson + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
