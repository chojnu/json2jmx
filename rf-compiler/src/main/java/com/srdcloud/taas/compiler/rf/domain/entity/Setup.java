package com.srdcloud.taas.compiler.rf.domain.entity;

import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

import java.util.List;

public class Setup implements IVisible {

    public List<Step> steps;

    public List<Arg> args;

    public List<Arg> getArgs() {
        return args;
    }

    public void setArgs(List<Arg> args) {
        this.args = args;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public void accept(IVisitor visiter) {

    }
}
