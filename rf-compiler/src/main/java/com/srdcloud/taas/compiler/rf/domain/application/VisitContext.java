package com.srdcloud.taas.compiler.rf.domain.application;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VisitContext {

    String name;
    VisitContext parent;

    Map<String, Object> map = new ConcurrentHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getProperty( String property_name){
        return map.get(property_name);
    }

    public VisitContext setProperty(String key, Object val){
        map.put(key, val);
        return  this;
    }

    public VisitContext getParent(){
        return this.parent;
    }

    public VisitContext getRoot(){
        VisitContext ctx = this;
        while(ctx.parent != null){
            ctx  = ctx.getParent();
        }

        return ctx;
    }

    VisitContext(VisitContext parent){

        this.parent=parent;
    }

}
