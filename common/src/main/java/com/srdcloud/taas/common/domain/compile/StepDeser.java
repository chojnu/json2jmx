package com.srdcloud.taas.common.domain.compile;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.srdcloud.taas.common.domain.compile.entity.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/***
 * jackson自定义反序列化工具类，由于step有多个子类，需要通过该工具类完成反序列
 */
public class StepDeser extends JsonDeserializer<Step> {
    @Override
    public Step deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        TreeNode tn = objectCodec.readTree(jsonParser);
        String type = ((TextNode) tn.get("type")).asText();
        String name = ((TextNode) tn.get("name")).asText();
        Step step = null;
        switch (type) {
            case "call_keyword":
                CallKeyword callKeyword = new CallKeyword();
                JsonParser jp = objectCodec.treeAsTokens(tn.get("params"));
                callKeyword.setParams(objectCodec.readValue(jp, new TypeReference<List<Arg>>() {
                }));
                step = callKeyword;
                break;
            case "stage":
                Stage stage = new Stage();
                stage.setStageName(((TextNode) tn.get("stageName")).asText());
                jp = objectCodec.treeAsTokens(tn.get("steps"));
                stage.setSteps(objectCodec.readValue(jp, new TypeReference<List<Step>>() {
                }));
                step = stage;
                break;
            case "if_else":
                IfElse ifElse = new IfElse();
                ifElse.setExpressions(((TextNode) tn.get("expressions")).asText());
                jp = objectCodec.treeAsTokens(tn.get("if_true"));
                ifElse.setIfTrue(objectCodec.readValue(jp, new TypeReference<List<Step>>() {
                }));
                if (tn.get("if_false") != null) {
                    jp = objectCodec.treeAsTokens(tn.get("if_false"));
                    ifElse.setIfFalse(objectCodec.readValue(jp, new TypeReference<List<Step>>() {
                    }));
                }
                step = ifElse;
                break;
            case "if_assert":
                IfAssert ifAssert = new IfAssert();
                jp = objectCodec.treeAsTokens(tn.get("assertions"));
                ifAssert.setAssertions(objectCodec.readValue(jp, new TypeReference<List<Map<String, String>>>() {
                }));
                jp = objectCodec.treeAsTokens(tn.get("if_true"));
                ifAssert.setIfTrue(objectCodec.readValue(jp, new TypeReference<List<Step>>() {
                }));
                if (tn.get("if_false") != null) {
                    jp = objectCodec.treeAsTokens(tn.get("if_false"));
                    ifAssert.setIfFalse(objectCodec.readValue(jp, new TypeReference<List<Step>>() {
                    }));
                }
                step = ifAssert;
                break;
            case "loop_in_range":
                LoopInRange loopInRange = new LoopInRange();
                if (tn.get("indexName") != null) {
                    loopInRange.setIndexName(((TextNode) tn.get("indexName")).asText());
                }
                loopInRange.setBeg(((IntNode) tn.get("beg")).intValue());
                loopInRange.setEnd(((IntNode) tn.get("end")).intValue());
                loopInRange.setSkip(((IntNode) tn.get("skip")).intValue());
                jp = objectCodec.treeAsTokens(tn.get("steps"));
                loopInRange.setSteps(objectCodec.readValue(jp, new TypeReference<List<Step>>() {
                }));
                step = loopInRange;
        }
        if (step != null) {
            step.setType(type);
            step.setName(name);
        }
        return step;
    }

}
