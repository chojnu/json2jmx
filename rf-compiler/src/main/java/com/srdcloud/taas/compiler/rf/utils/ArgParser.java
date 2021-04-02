package com.srdcloud.taas.compiler.rf.utils;

import com.srdcloud.taas.compiler.rf.domain.entity.Arg;
import com.srdcloud.taas.compiler.rf.domain.entity.Variable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ArgParser {



    public static List<Variable> parseJSON(JSONObject json, List<Variable> tmpVarList){

        if (json!=null){
            for (Iterator it = json.keys(); it.hasNext(); ) {
                String  key = (String) it.next();


            }
        }

        return  null;
    }

    public static List<Variable> parseJSONArray(JSONArray jsonArray, List<Variable> tmpVarList){

        return  null;
    }



    public static List<Variable> parse(Arg arg){
        List<Variable> list= new LinkedList<>();

        if ( arg.value instanceof JSONArray){

            return parseJSONArray((JSONArray) arg.value, list);
        }
        else if (arg.value instanceof  JSONObject){
            return parseJSON((JSONObject) arg.value, list);
        }

        return  null;
    }
}
