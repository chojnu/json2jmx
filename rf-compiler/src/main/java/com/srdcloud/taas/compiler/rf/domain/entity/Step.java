package com.srdcloud.taas.compiler.rf.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

@JsonDeserialize(using = StepDeserial.class)
public  class Step implements IVisible {

    @Override
    public void accept(IVisitor visiter) {

    }
}
