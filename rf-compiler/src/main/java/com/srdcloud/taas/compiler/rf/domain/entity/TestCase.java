package com.srdcloud.taas.compiler.rf.domain.entity;

import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

import java.util.List;

public class TestCase implements IVisible {

    public String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;
    public TestCaseInfo info;
    public Dependency dependency;
    public List<Step> steps;
    public Setup setup;
    public Teardown teardown;

    public List<Param> getInputs() {
        return inputs;
    }

    public void setInputs(List<Param> inputs) {
        this.inputs = inputs;
    }

    public List<Param> inputs;

    public TestCaseInfo getInfo() {
        return info;
    }

    public void setInfo(TestCaseInfo info) {
        this.info = info;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void accept(IVisitor visiter) {

    }
}
