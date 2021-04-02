package com.srdcloud.taas.compiler.rf.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;
import org.json.JSONObject;

import java.util.List;

public class Dependency implements IVisible {
    @JsonIgnore
    public List<JSONObject> schemas;
    //public List<Variable> env_vars;
    public TestData dataset;
    public List<Library> libraries;

    @Override
    public void accept(IVisitor visiter) {

    }
}
