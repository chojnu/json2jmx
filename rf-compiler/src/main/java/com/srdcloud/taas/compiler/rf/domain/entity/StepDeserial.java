package com.srdcloud.taas.compiler.rf.domain.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.List;

public class StepDeserial extends JsonDeserializer<Step> {
    @Override
    public Step deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        TreeNode tn = objectCodec.readTree(jsonParser);
        String type = ((TextNode) tn.get("type")).asText();
        String name = ((TextNode) tn.get("name")).asText();
        Step step = null;

        switch (type) {
            case "call_keyword":
                CallKeyword callKeyword = new CallKeyword(name);
                JsonParser jp = objectCodec.treeAsTokens(tn.get("params"));
                callKeyword.setArgs(objectCodec.readValue(jp, new TypeReference<List<Arg>>() {
                }));
                step = callKeyword;
                break;
            case "if_else":
                IfElse ifElse = new IfElse();
                ifElse.setCondition(((TextNode) tn.get("expressions")).asText());
                jp = objectCodec.treeAsTokens(tn.get("ifTrue"));
                ifElse.setIfTrue(objectCodec.readValue(jp, new TypeReference<List<Step>>() {
                }));
                if (tn.get("ifFalse") != null) {
                    jp = objectCodec.treeAsTokens(tn.get("ifFalse"));
                    ifElse.setIfFalse(objectCodec.readValue(jp, new TypeReference<List<Step>>() {
                    }));
                }
                step = ifElse;
                break;
            case "loop_in_range":
                LoopInRange loopInRange = new LoopInRange();

                loopInRange.setBeg(((IntNode) tn.get("beg")).intValue());
                loopInRange.setEnd(((IntNode) tn.get("end")).intValue());
                loopInRange.setSkip(((IntNode) tn.get("skip")).intValue());
                jp = objectCodec.treeAsTokens(tn.get("steps"));
                loopInRange.setSteps(objectCodec.readValue(jp, new TypeReference<List<Step>>() {
                }));
                step = loopInRange;
        }
        return step;
    }

}
