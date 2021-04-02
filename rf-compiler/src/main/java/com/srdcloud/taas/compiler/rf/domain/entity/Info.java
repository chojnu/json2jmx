package com.srdcloud.taas.compiler.rf.domain.entity;

import com.srdcloud.taas.compiler.rf.domain.interfaces.ITag;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVersion;

import java.util.List;

public class Info implements ITag, IVersion {

    public  Version ver;
    public List<Tag> tags;

    public Version getVer() {
        return ver;
    }

    public void setVer(Version ver) {
        this.ver = ver;
    }


    @Override
    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public void addTag(String tag) {

    }

    @Override
    public Version getVersion() {
        return getVer();
    }

    @Override
    public void setVersion(Version ver) {
        setVer(ver);
    }
}
