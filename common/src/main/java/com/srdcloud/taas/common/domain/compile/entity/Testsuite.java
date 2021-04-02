package com.srdcloud.taas.common.domain.compile.entity;

import com.srdcloud.taas.common.domain.compile.Visible;
import com.srdcloud.taas.common.domain.compile.Visitor;

import java.util.List;

public class Testsuite implements Visible {
    private String type;
    private String name;
    private Info info;
    private Dependency dependency;
    private List<Variable> envVars;
    private List<Testcase> testcases;
    private List<Keyword> keywords;
    private List<Step> setup;
    private List<Step> teardown;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }

    public List<Variable> getEnvVars() {
        return envVars;
    }

    public void setEnvVars(List<Variable> envVars) {
        this.envVars = envVars;
    }

    public List<Testcase> getTestcases() {
        return testcases;
    }

    public void setTestcases(List<Testcase> testcases) {
        this.testcases = testcases;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public List<Step> getSetup() {
        return setup;
    }

    public void setSetup(List<Step> setup) {
        this.setup = setup;
    }

    public List<Step> getTeardown() {
        return teardown;
    }

    public void setTeardown(List<Step> teardown) {
        this.teardown = teardown;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        for (int i = 0; i < keywords.size(); i++) {
            keywords.get(i).accept(visitor);
        }
        for (int i = 0; i < testcases.size(); i++) {
            testcases.get(i).accept(visitor);
        }
        visitor.postVisit();
    }
}
