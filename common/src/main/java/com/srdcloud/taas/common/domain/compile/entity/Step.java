package com.srdcloud.taas.common.domain.compile.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.srdcloud.taas.common.domain.compile.StepDeser;
import com.srdcloud.taas.common.domain.compile.Visible;

@JsonDeserialize(using = StepDeser.class)
public abstract class Step implements Visible {
    private String type;
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
