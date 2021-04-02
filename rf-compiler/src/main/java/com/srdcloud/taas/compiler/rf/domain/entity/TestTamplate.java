package com.srdcloud.taas.compiler.rf.domain.entity;

public class TestTamplate {
    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    public TestTamplate(String keywordName) {
        this.keywordName = keywordName;
    }

    public String keywordName;
}
