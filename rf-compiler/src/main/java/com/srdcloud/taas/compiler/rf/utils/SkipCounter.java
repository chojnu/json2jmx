package com.srdcloud.taas.compiler.rf.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class SkipCounter {
    AtomicInteger  skip_cnt = new AtomicInteger(0);
    String skip;
    public  SkipCounter(String skip){
        this.skip=skip;
    }
    public int push(){
        return skip_cnt.incrementAndGet();
    }

    public int pop(){
        return skip_cnt.decrementAndGet();
    }

    public int getSkip_cnt() {
        return skip_cnt.get();
    }

    public void setSkip_cnt(int skip_cnt) {
        this.skip_cnt.set(skip_cnt);
    }

    public String getSkip(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i =0; i< getSkip_cnt(); i++){
            stringBuilder.append(skip);
        }

        return stringBuilder.toString();
    }
}
