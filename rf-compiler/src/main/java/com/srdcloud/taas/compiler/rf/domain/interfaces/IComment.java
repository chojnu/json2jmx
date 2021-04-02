package com.srdcloud.taas.compiler.rf.domain.interfaces;

import com.srdcloud.taas.compiler.rf.domain.entity.Comment;

import java.util.List;

public interface IComment {

    void setComment(List<Comment> comments);
    List<Comment> getComment(String filter);

}
