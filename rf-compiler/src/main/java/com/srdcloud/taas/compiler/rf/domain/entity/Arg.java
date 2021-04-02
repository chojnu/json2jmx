package com.srdcloud.taas.compiler.rf.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

//@JsonDeserialize(using = ArgDeserial.class)
public class Arg implements IVisible {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String name;
    @JsonIgnore
    public String data_type;
    public Object value;

    @Override
    public void accept(IVisitor visiter) {

    }

/*    public Arg(String name, String data_type){
        this.name = name;
        this.data_type = data_type;
    }*/
}
