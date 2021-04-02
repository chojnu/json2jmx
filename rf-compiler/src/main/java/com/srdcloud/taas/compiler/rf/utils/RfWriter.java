package com.srdcloud.taas.compiler.rf.utils;

public class RfWriter {
    public  String SKIP="    ";
    public  String LINE_BREAK="\n";
    public  Integer nCursor;

    StringBuilder strBuilder=new StringBuilder();
    SkipCounter skipCounter=new SkipCounter(SKIP);
    public RfWriter indent(){
        strBuilder.append(skipCounter.getSkip());
        return  this;
    }

    public RfWriter skip(){
        strBuilder.append(SKIP);
        return  this;

    }

    public RfWriter newline(){
        strBuilder.append(LINE_BREAK);
        return  this;

    }

    public RfWriter enterDomain(){
        skipCounter.push();
        return  this;

    }

    public RfWriter exitDomain(){
        skipCounter.pop();
        return  this;

    }

    public RfWriter append(String word){
        strBuilder.append(word);
        return  this;
    }

    @Override
    public String toString() {
        return ""+strBuilder;
    }

    public void moveCursor(int offset){
        nCursor= nCursor + offset;
    }
}
