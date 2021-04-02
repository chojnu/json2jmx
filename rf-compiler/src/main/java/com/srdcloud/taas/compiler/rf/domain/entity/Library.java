package com.srdcloud.taas.compiler.rf.domain.entity;

import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

import java.util.List;

public class Library implements IVisible {

    public String name;
    public List<Arg> args;

    public Library() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Arg> getArgs() {
        return args;
    }

    public void setArgs(List<Arg> args) {
        this.args = args;
    }

    @Override
    public void accept(IVisitor visiter) {

    }
}
