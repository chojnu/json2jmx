package com.srdcloud.taas.compiler.rf.domain.entity;

import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

import java.util.List;

public class IfElse extends Step implements IVisible {
    public IfElse() {
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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

    public String condition;
    public List<Step> ifTrue;
    public List<Step> ifFalse;

    @Override
    public void accept(IVisitor visiter) {

    }
}
