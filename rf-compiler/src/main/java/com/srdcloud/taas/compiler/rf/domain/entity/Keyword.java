package com.srdcloud.taas.compiler.rf.domain.entity;

import com.srdcloud.taas.compiler.rf.domain.interfaces.IComment;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

import java.util.List;

public class Keyword implements IVisible, IComment {
    public String name;
    public String type;
    public List<Param> inputs;
    public List<Comment> comments;

    public Keyword(String name, String type, List<Param> inputs, List<Step> steps) {
        this.name = name;
        this.type = type;
        this.inputs = inputs;
        this.steps = steps;
    }

    public List<Param> outputs;
    public List<Step> steps;
    public Teardown teardown;

    public Keyword() {
    }

    public Keyword(String name, List<Step> steps, List<Param> inputs, List<Param> outputs, Teardown teardown){
        this.name = name;
        this.steps = steps;
        this.inputs = inputs;
        this.outputs = outputs;
        this.teardown = teardown;
    }

    public Keyword(String name, List<Step> steps, List<Param> inputs, List<Param> outputs){
        this.name = name;
        this.steps = steps;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public Keyword(String name, List<Step> steps){
        this.name = name;
        this.steps = steps;
    }
    @Override
    public void accept(IVisitor visiter) {

    }

    @Override
    public void setComment(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public List<Comment> getComment(String filter) {
        return comments;
    }
}
