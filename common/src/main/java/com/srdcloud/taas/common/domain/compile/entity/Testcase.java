package com.srdcloud.taas.common.domain.compile.entity;

import com.srdcloud.taas.common.domain.compile.Visible;
import com.srdcloud.taas.common.domain.compile.Visitor;

import java.util.List;

public class Testcase implements Visible {
    private String type;
    private String name;
    private Info info;
    private List<Variable> variables;
    private List<Param> inputs;
    private List<Step> steps;
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

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    public List<Param> getInputs() {
        return inputs;
    }

    public void setInputs(List<Param> inputs) {
        this.inputs = inputs;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
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

        if (setup != null && setup.size() > 0) {
            Stage stage = new Stage();
            stage.setSteps(setup);
            stage.setName("setup");
            visitor.visit(stage);
            visitor.postVisit();
        }

        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).accept(visitor);
        }
        if (teardown != null && teardown.size() > 0) {
            Stage stage = new Stage();
            stage.setSteps(teardown);
            stage.setName("teardown");
            visitor.visit(stage);
            visitor.postVisit();
        }
        visitor.postVisit();
    }
}
