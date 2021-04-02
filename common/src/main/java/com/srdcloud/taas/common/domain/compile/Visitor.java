package com.srdcloud.taas.common.domain.compile;

import com.srdcloud.taas.common.domain.compile.entity.*;

public interface Visitor {
    void visit(CallKeyword callKeyword);

    void visit(IfElse ifElse);

    void visit(Keyword keyword);

    void visit(IfAssert ifAssert);

    void visit(LoopInRange loopInRange);

    void visit(Stage stage);

    void visit(Testcase Testcase);

    void visit(Testsuite testsuite);

    void postVisit();
}
