package com.srdcloud.taas.common.event;

public class TestCaseCompileNotify {
    private long caseId;
    private String recordId;
    private String target;

    public long getCaseId() {
        return caseId;
    }

    public void setCaseId(long caseId) {
        this.caseId = caseId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "TestCaseCompileNotify{" +
                "caseId=" + caseId +
                ", recordId='" + recordId + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
