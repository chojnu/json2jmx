package com.srdcloud.taas.common.domain.compile.entity;

import com.srdcloud.taas.common.domain.compile.Visible;
import com.srdcloud.taas.common.domain.compile.Visitor;

import java.util.List;

public class Keyword implements Visible {
    private String type;
    private String name;
    private Info info;
    private List<Param> inputs;
    private List<Param> outputs;
    private List<Step> steps;

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

    public List<Param> getInputs() {
        return inputs;
    }

    public void setInputs(List<Param> inputs) {
        this.inputs = inputs;
    }

    public List<Param> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Param> outputs) {
        this.outputs = outputs;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).accept(visitor);
        }
        visitor.postVisit();
    }
}
