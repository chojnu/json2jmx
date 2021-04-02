package com.srdcloud.taas.common.domain.compile.entity;

public class Param {
    private String name;
    private String comment;
    private String datatype;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
}
