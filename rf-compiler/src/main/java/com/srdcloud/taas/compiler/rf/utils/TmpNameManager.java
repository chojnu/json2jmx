package com.srdcloud.taas.compiler.rf.utils;

import com.srdcloud.taas.compiler.rf.domain.entity.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TmpNameManager {

    Map<String, AtomicInteger> map = new ConcurrentHashMap<>();
    public String allocTmpName(String prefix){
        if (!map.containsKey(prefix)){
            map.put(prefix, new AtomicInteger(-1));
        }
        return String.format("%s_%d", prefix, map.get(prefix).incrementAndGet());
    }

    public Keyword createTmpKeyword(String prefix, List<Step> steps){
        Map<String, Param> varDict = new HashMap<>();
        //findArgs(steps, varDict);

        List<Param> list = new ArrayList<>();
        list.addAll(varDict.values());
        Keyword keyword = new Keyword(allocTmpName(prefix), steps, list, null);

        return  keyword;
    }
/*
    public Variable createTmpVariable( String prefix, Object valObj){

        return null;
    }

    public void findArgs(List<Step> steps, Map<String, Param>  varDict ){

        for ( Step step: steps
             ) {
            if (step instanceof CallKeyword){
                CallKeyword callKeyword = (CallKeyword) step;
                for ( Arg arg: callKeyword.args
                     ) {
                    findVariable(arg.value, varDict);
                }
            }
            else if(step instanceof  IfElse){
                IfElse ifElse = (IfElse) step;
                findArgs(ifElse.ifTrue, varDict);
                findArgs(ifElse.ifFalse, varDict);
            }
            else if(step instanceof  LoopInRange){
                LoopInRange loop = (LoopInRange) step;
                findArgs(loop.steps, varDict);
            }
        }
    }
        public  Map<String , Param> findVariable( Object obj, Map<String, Param> dict){

        if ( obj instanceof  String){
            Pattern p = Pattern.compile("\$\{[^}]+\}");
            Matcher m = p.matcher((String)obj);
            if (m.find()){
                for (int i=0; i< m.groupCount(); i++){

                    dict.put(m.group(i), new Param(m.group(i)));
                }
            }
        }
        else if (obj instanceof JSONArray){

            JSONArray jsonArray = (JSONArray) obj;
            for (int i=0; i< jsonArray.length(); i++){
                try {
                    findVariable(jsonArray.get(i), dict);
                }
                catch (JSONException e){

                }
            }
        }
        else if(obj instanceof  JSONObject){

            JSONObject json = (JSONObject) obj;

            for (Iterator<String> it = json.keys(); it.hasNext(); ){
                String key = it.next();
                try{
                    findVariable(json.get(key), dict);
                }
                catch (JSONException e){

                }
            }
        }

    }
*/
}
