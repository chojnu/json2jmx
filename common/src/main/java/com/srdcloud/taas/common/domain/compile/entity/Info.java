package com.srdcloud.taas.common.domain.compile.entity;

import java.util.List;

public class Info {
    private int ver;
    private List<String> tag;
    private String id;

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
