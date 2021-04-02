package com.srdcloud.taas.common.domain.compile.entity;

import com.srdcloud.taas.common.domain.compile.Visitor;

import java.util.List;
import java.util.Map;

public class IfAssert extends Step {
    private List<Map<String, String>> assertions;
    private List<Step> ifTrue;
    private List<Step> ifFalse;

    public List<Map<String, String>> getAssertions() {
        return assertions;
    }

    public void setAssertions(List<Map<String, String>> assertions) {
        this.assertions = assertions;
    }

    public List<Step> getIfTrue() {
        return ifTrue;
    }

    public void setIfTrue(List<Step> ifTrue) {
        this.ifTrue = ifTrue;
    }

    public List<Step> getIfFalse() {
        return ifFalse;
    }

    public void setIfFalse(List<Step> ifFalse) {
        this.ifFalse = ifFalse;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);

        Stage stage = new Stage();
        stage.setStageName("true");
        stage.setSteps(ifTrue);
        stage.accept(visitor);

        if (ifFalse == null || ifFalse.size() == 0)
            return;

        stage = new Stage();
        stage.setStageName("false");
        stage.setSteps(ifFalse);
        stage.accept(visitor);

        visitor.postVisit();
    }
}
