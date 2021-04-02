package com.srdcloud.taas.common.domain.compile.entity;

import com.srdcloud.taas.common.domain.compile.Visitor;

import java.util.List;

public class IfElse extends Step {
    private String expressions;
    private List<Step> ifTrue;
    private List<Step> ifFalse;

    public String getExpressions() {
        return expressions;
    }

    public void setExpressions(String expressions) {
        this.expressions = expressions;
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
