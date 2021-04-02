package com.srdcloud.taas.compiler.rf.domain.entity;

import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

import java.util.List;

public class LoopInRange extends Step implements IVisible {

    @Override
    public void accept(IVisitor visiter) {

    }


    public Integer beg;
    public Integer end;
    public Integer skip;
    public List<Step> steps;

    public LoopInRange(Integer beg, Integer end, Integer skip, List<Step> steps) {
        this.beg = beg;
        this.end = end;
        this.skip = skip;
        this.steps = steps;
    }

    public LoopInRange() {
    }

    public Integer getBeg() {
        return beg;
    }

    public void setBeg(Integer beg) {
        this.beg = beg;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
