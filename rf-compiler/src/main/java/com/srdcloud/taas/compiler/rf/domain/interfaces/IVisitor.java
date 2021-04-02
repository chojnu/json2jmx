package com.srdcloud.taas.compiler.rf.domain.interfaces;

import com.srdcloud.taas.compiler.rf.domain.application.VisitContext;
import com.srdcloud.taas.compiler.rf.domain.entity.*;

public interface  IVisitor{
    void visit(CallKeyword callKeyword, VisitContext ctx);
    void visit(Keyword keyword, VisitContext ctx);
    void visit(IfElse ifElse, VisitContext ctx);
    void visit(LoopInRange loopInRange, VisitContext ctx);
    void visit(Dependency dependency, VisitContext ctx);
    void visit(Param param, VisitContext ctx);
    void visit(Arg arg, VisitContext ctx);
    void visit(TestCase testcase, VisitContext ctx);
    void visit(TestSuit callKeyword, VisitContext ctx);
    void visit(Library library, VisitContext ctx);
    void visit(Setup setup, VisitContext ctx);

    void visit(TestSuitInfo info, VisitContext ctx);
    void visit(TestCaseInfo info, VisitContext ctx);

    void visit(Teardown teardown, VisitContext ctx);

    void visit(Step setp, VisitContext ctx);

    void visit(Variable variable, VisitContext ctx);
}