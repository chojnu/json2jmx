package com.srdcloud.taas.common.domain.compile.entity;

import java.util.List;

public class Library {
    private String name;
    private List<Arg> args;

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

}
