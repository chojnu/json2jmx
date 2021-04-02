package com.srdcloud.taas.compiler.rf.domain.entity;

import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

import java.util.List;

public class Teardown implements IVisible {
    public List<Step> steps;

    @Override
    public void accept(IVisitor visiter) {

    }
}
