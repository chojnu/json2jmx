package com.srdcloud.taas.common.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CaseCompileEvent {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private long caseId;
    private String recordId;
    private String eventName;
    private String compileFor;
    private String eventType;
    private String content;
    private String eventTime;

    public CaseCompileEvent() {
    }

    public CaseCompileEvent(long caseId, String recordId) {
        this.caseId = caseId;
        this.recordId = recordId;
    }

    private CaseCompileEvent set(String eventName, String eventType, String content) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.content = content;
        this.eventTime = FORMATTER.format(LocalDateTime.now());
        return this;
    }

    public CaseCompileEvent setToCompiling() {
        return set("compiling", "normal", "jmx脚本生成开始");
    }

    public CaseCompileEvent setToFinished() {
        return set("finished", "normal", "jmx脚本生成完成");
    }

    public CaseCompileEvent setToFailed(String reason) {
        return set("finished", "fail", reason);
    }

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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCompileFor() {
        return compileFor;
    }

    public void setCompileFor(String compileFor) {
        this.compileFor = compileFor;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "CaseCompileEvent{" +
                "caseId=" + caseId +
                ", recordId='" + recordId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", compileFor='" + compileFor + '\'' +
                ", eventType='" + eventType + '\'' +
                ", content='" + content + '\'' +
                ", eventTime='" + eventTime + '\'' +
                '}';
    }
}
