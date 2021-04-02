package com.srdcloud.taas.compiler.rf.domain.entity;


import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

import java.util.List;

public class CallKeyword extends Step implements IVisible {

    public List<Arg> args;

    public String name;

    public CallKeyword(String name, List<Arg> args){
        this.name = name;
        this.args = args;
    }

    public CallKeyword(String name) {
        this.name = name;
    }

    public List<Arg> getArgs() {
        return args;
    }

    public void setArgs(List<Arg> args) {
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void accept(IVisitor visiter) {

    }
}
