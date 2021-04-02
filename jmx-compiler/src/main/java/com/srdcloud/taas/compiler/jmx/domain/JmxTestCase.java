package com.srdcloud.taas.compiler.jmx.domain;

public class JmxTestCase {
    private long id;
    private String status;
    private String testCaseJson;
    private String reason;

    public boolean compile() {
        status = "compiled";
        return true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
