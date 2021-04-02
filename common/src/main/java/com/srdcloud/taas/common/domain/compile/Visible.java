package com.srdcloud.taas.common.domain.compile;


public interface Visible {
    void accept(Visitor visitor);
}
