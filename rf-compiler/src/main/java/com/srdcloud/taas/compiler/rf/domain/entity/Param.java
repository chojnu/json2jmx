package com.srdcloud.taas.compiler.rf.domain.entity;

import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

public class Param implements IVisible {
    public String name;

    public Param(String name, String type, Object default_value, String comment) {
        this.name = name;
        this.type = type;
        this.default_value = default_value;
        this.comment = comment;
    }

    public Param() {
    }

    public String type;
    public Object default_value;
    public String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public  Param(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public Param(String name, String type, Object default_value) {
        this.name = name;
        this.type = type;
        this.default_value = default_value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getDefault_value() {
        return default_value;
    }

    public void setDefault_value(Object default_value) {
        this.default_value = default_value;
    }

    public  Param(String name, Object default_value){
        this.name = name;
        this.default_value = default_value;
    }
    @Override
    public void accept(IVisitor visiter) {

    }
}
