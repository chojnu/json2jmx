package com.srdcloud.taas.common.domain.compile.entity;

import com.srdcloud.taas.common.domain.compile.Visitor;

import java.util.List;

public class Stage extends Step {
    private String stageName;
    private List<Step> steps;

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
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
