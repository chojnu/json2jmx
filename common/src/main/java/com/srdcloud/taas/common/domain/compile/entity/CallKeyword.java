package com.srdcloud.taas.common.domain.compile.entity;

import com.srdcloud.taas.common.domain.compile.Visitor;

import java.util.List;

public class CallKeyword extends Step {
    private List<Arg> params;

    public List<Arg> getParams() {
        return params;
    }

    public void setParams(List<Arg> params) {
        this.params = params;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        visitor.postVisit();
    }
}
