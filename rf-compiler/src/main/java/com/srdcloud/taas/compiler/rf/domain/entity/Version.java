package com.srdcloud.taas.compiler.rf.domain.entity;

import java.util.UUID;

public class Version {
    public UUID id;

    public Version(String ver) {
        this.ver=ver;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String ver;
}
