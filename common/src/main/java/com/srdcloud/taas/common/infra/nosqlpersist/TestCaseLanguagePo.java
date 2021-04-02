package com.srdcloud.taas.common.infra.nosqlpersist;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("TestCaseLanguageCollection")
public class TestCaseLanguagePo {
    @Id
    private String id;
    private long caseId;
    private String testCaseJson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCaseId() {
        return caseId;
    }

    public void setCaseId(long caseId) {
        this.caseId = caseId;
    }

    public String getTestCaseJson() {
        return testCaseJson;
    }

    public void setTestCaseJson(String testCaseJson) {
        this.testCaseJson = testCaseJson;
    }
}
