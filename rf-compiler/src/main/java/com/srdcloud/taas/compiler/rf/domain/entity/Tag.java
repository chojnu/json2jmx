package com.srdcloud.taas.compiler.rf.domain.entity;

import java.util.UUID;

public class Tag {
    public UUID id;
    public String tag;

    public Tag(String tag) {
        this.tag = tag;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
