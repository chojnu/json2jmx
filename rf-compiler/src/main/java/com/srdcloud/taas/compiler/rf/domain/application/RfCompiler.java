package com.srdcloud.taas.compiler.rf.domain.application;

import com.srdcloud.taas.compiler.rf.domain.entity.*;
import com.srdcloud.taas.compiler.rf.domain.interfaces.IVisitor;
import com.srdcloud.taas.compiler.rf.utils.RfWriter;
import com.srdcloud.taas.compiler.rf.utils.TmpNameManager;
import org.json.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RfCompiler implements IVisitor {


    public RfWriter keywordWriter = new RfWriter();

    public RfWriter testcaseWriter = new RfWriter();

    public RfWriter settingWriter = new RfWriter();

    public RfWriter variableWriter = new RfWriter();

    public TmpNameManager tmpNameManager = new TmpNameManager();

    @Override
    public  void visit(TestSuit testsuit, VisitContext ctx){

        settingWriter.append("*** Settings ****").newline();
        settingWriter.append("Documentation").skip().append(testsuit.getName()).newline();

        ctx.setProperty("writer", settingWriter);
        this.visit(testsuit.info, ctx);
        this.visit(testsuit.dependency, ctx);

        this.visit(testsuit.setup, ctx);

        this.visit(testsuit.teardown, ctx);

        if (testsuit.env_vars!=null){
            variableWriter.newline().append("*** Variables ***").newline();

            for (Variable var: testsuit.env_vars
            ) {
                visit(var, new VisitContext(ctx).setProperty("writer", variableWriter));
            }
        }


        if (testsuit.keywords!=null){
            keywordWriter.newline().append("*** Keywords ***").newline();
            for (Keyword keyword : testsuit.keywords
            ) {
                this.visit(keyword, ctx);
            }
        }


        if (testsuit.testcases!=null){
            testcaseWriter.newline().append("*** Cases ***").newline();
            for (TestCase testcase: testsuit.testcases
            ) {
                this.visit(testcase, ctx);

            }
        }
    }
    @Override
    public void visit(TestCase testcase, VisitContext ctx){

        visit(testcase.info, ctx);
        visit(testcase.dependency, ctx);



        testcaseWriter.append(testcase.name).newline();
        testcaseWriter.enterDomain();
        visit(testcase.setup, new VisitContext(ctx).setProperty("writer", testcaseWriter));
        visit(testcase.teardown, new VisitContext(ctx).setProperty("writer", testcaseWriter));

        for (Step step: testcase.steps
        ) {
            visit(step,
                    new VisitContext(ctx).setProperty("writer", testcaseWriter));
        }

        testcaseWriter.exitDomain();

    }

    @Override

    public void visit(TestCaseInfo info, VisitContext ctx) {
        if (info == null){
            return;
        }
        if (info.ver!=null){
            testcaseWriter.indent();
            testcaseWriter.append("[Documentation]").skip().append("Version").skip().append(info.ver.ver);
            testcaseWriter.newline();
        }
        if (info.tags!=null && info.tags.size()>0){
            testcaseWriter.indent().append("[Tags]");
            for ( Tag tag: info.tags){
                testcaseWriter.skip();
                testcaseWriter.append(tag.tag);
            }
            testcaseWriter.newline();
        }
    }

    @Override
    public void visit(Keyword keyword, VisitContext ctx) {

        RfWriter writer = keywordWriter;
        writer.append(keyword.name);
        writer.newline();
        writer.enterDomain();
        if (keyword.comments!=null && keyword.comments.size()>0){
            writer.append("[Documentation]");
            for (int i=0; i< keyword.comments.size(); i++){
                writer.skip();
                writer.append(keyword.comments.get(i).comment);
                writer.newline();
                if (i!=keyword.comments.size()-1){
                    writer.append("...");
                }
            }
        }
        if (keyword.inputs!=null && keyword.inputs.size()>0){

            writer.indent();
            writer.append("[Arguments]");

            for ( Param param: keyword.inputs
                 ) {
                    writer.skip();
                    visit(param, new VisitContext(ctx).setProperty("writer",writer));
                }
            writer.newline();
        }

        if (keyword.steps!=null && keyword.steps.size()>0){

            for (Step step: keyword.steps
                 ) {
                visit(step, new VisitContext(ctx).setProperty("writer",writer));
                writer.newline();
            }

        }

        if (keyword.outputs!=null && keyword.outputs.size()>0){
            writer.indent();
            writer.append("[Return]");

            for (Param param: keyword.outputs
                 ) {
                writer.skip();
                visit(param, new VisitContext(ctx).setProperty("writer", writer));

            }
            writer.newline();
        }

        if (keyword.teardown!=null){
            visit(keyword.teardown, new VisitContext(ctx).setProperty("writer", writer));
        }


    }

    @Override
    public void visit(Dependency dependency, VisitContext ctx) {

        if (dependency==null)
            return;

        if (dependency.libraries!=null){
            for (Library lib: dependency.libraries
            ) {
                visit(lib, ctx);
            }
        }


    }

    @Override
    public void visit(Param param, VisitContext ctx) {
        RfWriter writer = (RfWriter)ctx.getProperty("writer");

        writer.append("${"+param.name+"}");
        if (param.default_value != null){
            writer.append("=");
            writer.append(param.default_value.toString());
        }
    }

    @Override
    public void visit(Arg arg, VisitContext ctx) {

        if (arg==null)
            return;

        RfWriter writer = (RfWriter)ctx.getProperty("writer");

        if (arg.name != null && !arg.name.isEmpty()){
            writer.append(arg.name+"=");
        }
        writer.append(arg.value.toString());

        if (arg.value instanceof String){
        }
        else if(arg.value instanceof JSONObject){

        }
        else if(arg.value instanceof JSONArray){

        }
    }

    @Override
    public  void visit(Library library, VisitContext ctx){
        settingWriter.append("Library").skip().append(library.name);

        for (Arg arg:library.args
             ) {
            settingWriter.skip().append(arg.toString());
        }

    }
    @Override
    public void visit(TestSuitInfo info, VisitContext ctx) {

        if (info == null){
            return;
        }

        if (info.ver!=null){
            settingWriter.append("Metadata").skip().append("Version").skip().append(info.ver.ver);
            settingWriter.newline();
        }
        if (info.tags!=null && info.tags.size()>0){
            settingWriter.append("Default Tags");
            for ( Tag tag: info.tags){
                settingWriter.skip();
                settingWriter.append(tag.tag);
            }
            settingWriter.newline();
        }
    }



    @Override
    public void visit(Setup setup, VisitContext ctx) {

        if (setup ==null)
            return;
        RfWriter writer = (RfWriter)ctx.getProperty("writer");

        if (writer == settingWriter){
            writer.append("Test Setup").skip().skip();
        }
        else{

            writer.indent();
            writer.append("[Setup]").skip();
        }

        if (setup.steps!=null ){

            if (setup.steps.size() ==1){
                visit(setup.steps.get(0), ctx);
            }
            else{
                Keyword tmpKeyword = new Keyword(tmpNameManager.allocTmpName("Tmp_Keyword_Setup"), setup.steps);
                visit(tmpKeyword, ctx.getRoot());
                writer.append(tmpKeyword.name);
            }
        }
        writer.newline();

    }

    @Override
    public void visit(Teardown teardown, VisitContext ctx) {
        if (teardown == null)
            return;
        RfWriter writer = (RfWriter)ctx.getProperty("writer");

        if (writer == settingWriter){
            writer.append("Test Teardown").skip().skip();
        }
        else{

            writer.indent();
            writer.append("[Teardown]").skip();
        }

        if (teardown.steps!=null ){

            if (teardown.steps.size() ==1){
                visit(teardown.steps.get(0), ctx);
            }
            else{
                Keyword tmpKeyword = new Keyword(tmpNameManager.allocTmpName("Tmp_Keyword_Setup"), teardown.steps);
                visit(tmpKeyword, ctx.getRoot());
                writer.append(tmpKeyword.name);
            }
        }
        writer.newline();
    }


    @Override
    public void visit( Step step, VisitContext ctx){
        if (step instanceof CallKeyword){
            visit((CallKeyword) step, ctx);
        }
        else if (step instanceof  IfElse){
            visit((IfElse) step, ctx);
        }
        else if( step instanceof  LoopInRange){
            visit((LoopInRange) step, ctx);
        }
    }
    @Override
    public void visit( IfElse ifElse, VisitContext ctx) {

        RfWriter writer = (RfWriter)ctx.getProperty("writer");
        writer.indent().append("IF").skip().append(ifElse.condition);
        writer.newline();
        boolean isOK1=false, isOK2=false;
        if (ifElse.ifTrue != null && ifElse.ifTrue.size()> 0 ){
            writer.enterDomain();
            for (Step step: ifElse.ifTrue
                 ) {
                visit(step, ctx);
            }
            writer.exitDomain();
        }

        if (ifElse.ifFalse != null && ifElse.ifFalse.size()> 0 ){

            writer.enterDomain();
            for (Step step: ifElse.ifFalse
            ) {
                visit(step, ctx);
            }
            writer.exitDomain();
        }
        writer.newline().indent().append("END").newline();

    }

    @Override
    public void visit(LoopInRange loopInRange, VisitContext ctx) {
        RfWriter writer = (RfWriter)ctx.getProperty("writer");
        writer.indent().append("FOR").skip().append("IN RANGE").skip();
        if (loopInRange.beg != null){
            writer.append(loopInRange.beg.toString());
            writer.skip();
        }
        if (loopInRange.end != null){
            writer.append(loopInRange.end.toString());
            writer.skip();

        }
        if (loopInRange.skip != null){
            writer.append(loopInRange.skip.toString());
            writer.skip();

        }
        writer.newline();

        writer.enterDomain();
        for (Step step: loopInRange.steps
             ) {
            visit(step, ctx);
        }
        writer.exitDomain();

        writer.indent().append("END");
        writer.newline();

    }

    @Override
    public void visit(CallKeyword calkeyword, VisitContext ctx){
        if (calkeyword==null)
            return;

        RfWriter writer = (RfWriter)ctx.getProperty("writer");
        writer.indent();

        writer.append(calkeyword.name).skip();

        for (Arg arg: calkeyword.args
        ) {
            visit(arg, ctx);
            writer.skip();
        }
        writer.newline();
    }

    @Override
    public  void visit(Variable variable, VisitContext ctx){
        if (variable==null)
            return;
        RfWriter writer = (RfWriter)ctx.getProperty("writer");

        if (variable.value instanceof String){

        }
        if (writer==settingWriter){

        }
        else{

        }

    }

    public void compile(TestSuit testsuit, String ofile) throws IOException {

        visit(testsuit, new VisitContext(null));
        File f = new File(ofile);
        FileOutputStream ostream = new FileOutputStream(f);
        ostream.write(settingWriter.toString().getBytes());
        ostream.write(variableWriter.toString().getBytes());
        ostream.write(testcaseWriter.toString().getBytes());
        ostream.write(keywordWriter.toString().getBytes());

        ostream.flush();
        ostream.close();

    }

}
