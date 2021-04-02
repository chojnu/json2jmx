package com.srdcloud.taas.common.domain.compile.entity;

import java.util.List;

public class Dependency {
    private Object schema;
    private Dataset dataset;
    private List<Library> libraries;
    private List<Include> includes;

    public Object getSchema() {
        return schema;
    }

    public void setSchema(Object schema) {
        this.schema = schema;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }

    public List<Include> getIncludes() {
        return includes;
    }

    public void setIncludes(List<Include> includes) {
        this.includes = includes;
    }

}
