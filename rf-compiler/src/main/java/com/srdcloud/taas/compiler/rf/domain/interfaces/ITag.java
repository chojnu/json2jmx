package com.srdcloud.taas.compiler.rf.domain.interfaces;

import com.srdcloud.taas.compiler.rf.domain.entity.Tag;

import java.util.List;
import java.util.Set;

public interface ITag {

    List<Tag> getTags();
    void setTags(List<Tag> tags);
    void addTag(String tag);
}
