package com.srdcloud.taas.compiler.rf.domain.entity;

import java.util.List;

public class TestSuit{

    public String type;
    public TestSuitInfo info;
    public List<Keyword> keywords;
    public Dependency dependency;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Variable> env_vars;
    public TestTamplate testTemplate;
    public Setup setup;

    public TestTamplate getTestTemplate() {
        return testTemplate;
    }

    public void setTestTemplate(TestTamplate testTemplate) {
        this.testTemplate = testTemplate;
    }

    public  String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestSuitInfo getInfo() {
        return info;
    }

    public void setInfo(TestSuitInfo info) {
        this.info = info;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }

    public List<Variable> getEnv_vars() {
        return env_vars;
    }

    public void setEnv_vars(List<Variable> env_vars) {
        this.env_vars = env_vars;
    }

    public Setup getSetup() {
        return setup;
    }

    public void setSetup(Setup setup) {
        this.setup = setup;
    }

    public Teardown getTeardown() {
        return teardown;
    }

    public void setTeardown(Teardown teardown) {
        this.teardown = teardown;
    }

    public List<TestCase> getTestcases() {
        return testcases;
    }

    public void setTestcases(List<TestCase> testcases) {
        this.testcases = testcases;
    }

    public Teardown teardown;
    public List<TestCase> testcases;
}
