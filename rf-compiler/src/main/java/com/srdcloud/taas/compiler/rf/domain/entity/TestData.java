package com.srdcloud.taas.compiler.rf.domain.entity;

import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisible;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;

public class TestData implements IVisible {

    public String id;
    public String path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void accept(IVisitor visiter) {

    }
}
