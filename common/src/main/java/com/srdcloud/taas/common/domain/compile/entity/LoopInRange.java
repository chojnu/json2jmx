package com.srdcloud.taas.common.domain.compile.entity;

import com.srdcloud.taas.common.domain.compile.Visitor;

import java.util.List;

public class LoopInRange extends Step {
    private String indexName;
    private int beg;
    private int end;
    private int skip;
    private List<Step> steps;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public int getBeg() {
        return beg;
    }

    public void setBeg(int beg) {
        this.beg = beg;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
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
