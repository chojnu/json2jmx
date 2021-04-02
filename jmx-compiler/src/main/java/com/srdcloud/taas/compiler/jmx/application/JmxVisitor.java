package com.srdcloud.taas.compiler.jmx.application;

import com.fasterxml.jackson.databind.node.TextNode;
import com.srdcloud.taas.common.domain.compile.Visitor;
import com.srdcloud.taas.common.domain.compile.entity.*;
import com.srdcloud.taas.compiler.jmx.infra.jmeterutil.JmxUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;

import java.util.*;

public class JmxVisitor implements Visitor {
    private HashTree hashTree = null;
    private List<HashTree> hashTrees = new ArrayList<>();
    private List<TestElement> testElements = new ArrayList<>();
    private int loopCnt = 0;
    private int callCnt = 0;
    private int assertCnt = 0;
    private Map<String, List<Param>> kwParam = new HashMap<>();
    private String testPlanName;
    private Dataset dataset;

    public JmxVisitor() {
    }

    public HashTree getHashTree() {
        return hashTree;
    }

    public void setHashTree(HashTree hashTree) {
        this.hashTree = hashTree;
    }

    /***
     * 把jemter组件添加到hashtree中的当前组件的子节点中，并设置添加的组件设置为当前组件
     * @param testElement 添加到hashtree中的jmeter组件
     * @return 返回自身方便连续调用add或end方法
     */
    private JmxVisitor add(TestElement testElement) {
        if (hashTrees.size() > 0) {
            HashTree hashTree = hashTrees.get(hashTrees.size() - 1).add(testElement);
            hashTrees.add(hashTree);
        } else {
            hashTree = new ListedHashTree();
            hashTrees.add(hashTree.add(testElement));
        }
        testElements.add(testElement);
        return this;
    }

    /***
     * 把前组件的父节点设置为当前组件
     * @return  返回自身方便连续调用add或end方法
     */
    public JmxVisitor end() {
        HashTree ht = hashTrees.remove(hashTrees.size() - 1);
        TestElement testElement = testElements.remove(testElements.size() - 1);
        return this;
    }

    /***
     * 实现规范setvariable关键字
     * @param args args列表中的第一个元素的value为变量名，
     *             第二个元素的value为变量的取值
     */
    public void setVariable(List<Arg> args) {
        String name = args.get(0).getValue().toString().trim();
        String value = args.get(1).getValue().toString().trim();
        String temp = "vars.put(%s, %s);";
        String command = JmxUtils.stringFormat(temp, name, value);
        add(JmxUtils.genJSR223Sampler("set variable", command, false));
    }

    /***
     * 实现规范getfield关键字
     * @param args args列表中的第一个元素的value为变量名，
     *             第二个元素的value为提取值所在的变量名，
     *             第三个元素的value为提取值所在的变量中的路径，使用jsonpath格式来指定
     */
    public void getField(List<Arg> args) {
        String varName = args.get(0).getValue().toString().trim();
        String refVarName = args.get(1).getValue().toString().trim();
        String expression = args.get(2).getValue().toString().trim();
        String temp = "import com.jayway.jsonpath.Configuration;\n" +
                "import com.jayway.jsonpath.DocumentContext;\n" +
                "import com.jayway.jsonpath.JsonPath;\n" +
                "import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;\n" +
                "import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;\n" +
                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                "String json = vars.get(%s);\n" +
                "if(json == null) {\n" +
                "throw new Exception(%s);}\n" +
                "Configuration conf = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();\n" +
                "DocumentContext ext = JsonPath.using(conf).parse(json);\n" +
                "String value = ((JsonNode)ext.read(%s)).textValue();\n" +
                "vars.put(%s, value);\n";
        add(JmxUtils.genJSR223Sampler("get field", JmxUtils.stringFormat(temp, refVarName, "var not found: " + refVarName, expression, varName), false));
    }

    /***
     * 实现规范http关键字
     * @param args args列表中的第一个元素的value为请求路径，
     *             第二个元素的value为请求方法，
     *             第三个元素的value为请求header，结构为list<map<string,string>>，
     *             map中包含的key有name、value，分别表示header的key和value
     */
    public void http(List<Arg> args) {
        String api = args.get(0).getValue().toString().trim();
        String method = args.get(1).getValue().toString().trim().toUpperCase();
        List<Map<String, String>> list = (List<Map<String, String>>) args.get(2).getValue();
        Map<String, String> headers = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> jn = list.get(i);
            headers.put(jn.get("name"), jn.get("value"));
        }
        String body = args.get(3).getValue().toString();
        Arguments arguments = new Arguments();
        HTTPArgument httpArgument = new HTTPArgument();
        httpArgument.setAlwaysEncoded(false);
        httpArgument.setValue(body);
        httpArgument.setMetaData("=");
        arguments.addArgument(httpArgument);
        add(JmxUtils.genHTTPSampleProxy(api, method, arguments))
                .add(JmxUtils.genHeaderManager(headers)).end().end();
        String saveRespScript = "import org.apache.jmeter.samplers.SampleResult as srclass;\n" +
                "import com.jayway.jsonpath.Configuration;\n" +
                "import com.jayway.jsonpath.DocumentContext;\n" +
                "import com.jayway.jsonpath.JsonPath;\n" +
                "import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;\n" +
                "import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;\n" +
                "srclass sr = ctx.getPreviousResult();\n" +
                "Map<String, String> httpResult = new HashMap();\n" +
                "httpResult.put('responseCode', sr.getResponseCode());\n" +
                "httpResult.put('responseMessage', sr.getResponseMessage());\n" +
                "httpResult.put('responseDataAsString', sr.getResponseDataAsString());\n" +
                "httpResult.put('responseHeaders', sr.getResponseHeaders());\n" +
                "vars.putObject('__lastHttpResult', httpResult);\n" +
                "String[] headerStrs = httpResult.get('responseHeaders').tokenize('\\n');\n" +
                "Map<String, String> headers = new HashMap();\n" +
                "for(int i = 1; i < headerStrs.length; i++) {\n" +
                "int index = headerStrs[i].indexOf(': ');\n" +
                "String _key = headerStrs[i].substring(0, index);\n" +
                "String _value = headerStrs[i].substring(index + 2);\n" +
                "headers.put(_key, _value);\n" +
                "}\n" +
                "String body = httpResult.get('responseDataAsString');\n" +
                "Configuration conf = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();\n" +
                "DocumentContext ext = JsonPath.using(conf).parse(body);\n" +
                "vars.putObject('__parseHeaders', headers);\n" +
                "vars.putObject('__parseBody', ext);\n";
        add(JmxUtils.genJSR223Sampler("save resp", saveRespScript, true));
    }

    /***
     * 实现规范httpextract关键字
     * @param args args列表中的第一个元素的value结构为list<map<string,string>>，
     *             map中包含的key有varname、field、param，
     *             varname表示变量名，
     *             field表示提取的位置，可选值为header、body、code，
     *             param表示提取路径，当field为header时为header的key，
     *             当field为body时为body的jsonpath路径
     */
    public void httpExtracts(List<Arg> args) {
        StringBuilder sb = new StringBuilder();
        sb.append("import com.jayway.jsonpath.DocumentContext;\n" +
                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                "Map<String, String> httpResult = vars.getObject('__lastHttpResult');\n" +
                "Map<String, String> headers = vars.getObject('__parseHeaders');\n" +
                "DocumentContext ext = vars.getObject('__parseBody');\n" +
                "String value;\n");
        List<Map<String, String>> list = (List<Map<String, String>>) args.get(0).getValue();
        for (int i = 0; i < list.size(); i++) {
            String varName = list.get(i).get("varname");
            String field = list.get(i).get("field");
            String param = list.get(i).get("param");
            sb.append(httpExtract(varName, field, param));
        }
        add(JmxUtils.genJSR223Sampler("http extract", sb.toString(), false));
    }

    public String httpExtract(String varName, String field, String param) {
        String script;
        if ("header".equals(field)) {
            String tmp = "value = headers.get(%s);\n" +
                    "if(value != null) {\n" +
                    "vars.put(%s, value);\n" +
                    "}\n";
            script = JmxUtils.stringFormat(tmp, param, varName);
        } else if ("body".equals(field)) {
            String tmp = "value = ((JsonNode)ext.read(%s)).textValue();\n" +
                    "vars.put(%s, value);\n";
            script = JmxUtils.stringFormat(tmp, param, varName);
        } else {
            String tmp = "value = httpResult.get('responseCode');\n" +
                    "vars.put(%s, value);\n";
            script = JmxUtils.stringFormat(tmp, varName);
        }
        return script;
    }

    /***
     * 调用自定义关键字时，先把变量存到另外的变量中，再设置关键字需要的变量
     * @param params 自定义关键字的参数列表
     * @param args 调用自定义关键字时的参数值
     * */
    public String setParam(List<Param> params, List<Arg> args, int _callCnt) {
        String tmp = "if(vars.get(%s)){vars.put(%s, vars.get(%s));vars.put(%s, %s);}\n";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            String paramName = params.get(i).getName();
            String value = args.get(i).getValue().toString();
            String oldParamName = paramName + "_" + _callCnt;
            callCnt++;
            sb.append(JmxUtils.stringFormat(tmp, paramName, oldParamName, paramName, paramName, value));
        }
        return sb.toString();
    }

    /***
     * 调用自定义关键字结束后，把变量设置为原来调用前的值
     * @param params 自定义关键字的参数列表
     * */
    public String resetParam(List<Param> params, int _callCnt) {
        String tmp = "if(vars.get(%s)){vars.put(%s, vars.get(%s));vars.remove(%s);}\n";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            String paramName = params.get(i).getName();
            String oldParamName = paramName + "_" + _callCnt;
            sb.append(JmxUtils.stringFormat(tmp, oldParamName, paramName, oldParamName, oldParamName));
        }
        return sb.toString();
    }

    /***
     * 实现规范httpassert关键字
     * @param args args列表中的第一个元素的value结构为list<map<string,string>>，
     *             map中包含的key有field、param、value、operator，
     *             field表示需要断言的位置，可选值为header、body、code，
     *             param表示提取路径，当field为header时为header的key，
     *             当field为body时为body的jsonpath路径，
     *             value表示param的期望值，
     *             operator表示操作符，可选值为==、!=、>、<、regex
     */
    public void httpAssert(List<Arg> args) {
        StringBuilder sb = new StringBuilder();
        sb.append("import com.jayway.jsonpath.DocumentContext;\n" +
                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                "Map<String, String> httpResult = vars.getObject('__lastHttpResult');\n" +
                "Map<String, String> headers = vars.getObject('__parseHeaders');\n" +
                "DocumentContext ext = vars.getObject('__parseBody');\n");
        List<Map<String, String>> list = (List<Map<String, String>>) args.get(0).getValue();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> m = list.get(i);
            String field = m.get("field");
            String param = m.get("param");
            String value = m.get("value");
            TextNode asStr = new TextNode("");
            if(JmxUtils.isStrNumeric(value)) {
                asStr = new TextNode("as Float");
            }
            String tmp;
            String paramName = JmxUtils.filterNotAlpha(param);
            if ("header".equals(field)) {
                tmp = JmxUtils.stringFormat("Object hd_%s = headers.get(%s) %s ;\n", new TextNode(paramName), param, asStr);
                m.put("field", "hd_" + paramName);
            } else if ("body".equals(field)) {
                tmp = JmxUtils.stringFormat("Object bd_%s = ((JsonNode)ext.read(%s)).textValue() %s ;\n", new TextNode(paramName), param, asStr);
                m.put("field", "bd_" + paramName);
            } else {
                tmp = JmxUtils.stringFormat("Object st_code = httpResult.get('responseCode') %s ; \n", asStr) ;
                m.put("field", "st_code");
            }
            sb.append(tmp);
        }
        String condition = JmxUtils.joinAssertion(list, true, assertCnt);
        assertCnt++;
        sb.append(condition);
        add(JmxUtils.genJSR223Sampler("http assert", sb.toString(), false));
    }


    /***
     * vistor遇到callkeyword时，根据callkeyword的name属性区分不同类型的关键字调用
     * @param callKeyword callkeyword中的params属性对于不同关键字有不同的结构
     */
    @Override
    public void visit(CallKeyword callKeyword) {
        if ("http".equals(callKeyword.getName())) {
            http(callKeyword.getParams());
        } else if ("set_variable".equals(callKeyword.getName())) {
            setVariable(callKeyword.getParams());
        } else if ("get_field".equals(callKeyword.getName())) {
            getField(callKeyword.getParams());
        } else if ("assert".equals(callKeyword.getName())) {
            Object oa = callKeyword.getParams().get(0).getValue();
            String condition;
            if (oa instanceof String) {
                condition = oa.toString();
            } else {
                condition = JmxUtils.joinAssertion((List<Map<String, String>>) oa, false, assertCnt);
                assertCnt++;
            }
            add(JmxUtils.genJSR223Sampler("assert", condition, false));
        } else if ("http_extract".equals(callKeyword.getName())) {
            httpExtracts(callKeyword.getParams());
        } else if ("http_assert".equals(callKeyword.getName())) {
            httpAssert(callKeyword.getParams());
        } else if (kwParam.containsKey(callKeyword.getName().toLowerCase())) {
            List<Param> ps = kwParam.get(callKeyword.getName().toLowerCase());
            int _callCnt = callCnt;
            callCnt++;
            String script = setParam(ps, callKeyword.getParams(), _callCnt);
            add(JmxUtils.genJSR223Sampler("set param", script, true)).end();
            add(JmxUtils.genModuleController("call keyword", testPlanName, callKeyword.getName())).end();
            script = resetParam(ps, _callCnt);
            add(JmxUtils.genJSR223Sampler("reset param", script, true));
        }
    }

    public static String replaceVar(String expression) {
        return "${__groovy(" + expression + ")}";
    }

    @Override
    public void visit(IfElse ifElse) {
        add(JmxUtils.genSwitchController("if", replaceVar(ifElse.getExpressions())));
    }

    /***
     * vistor遇到keyword时，把自定义关键字名和参数作为key和value存入kwParam变量，
     * 当调用自定义关键字时，从kwParam中获取自定义关键的参数列表
     */
    @Override
    public void visit(Keyword keyword) {
        kwParam.put(keyword.getName().toLowerCase(), keyword.getInputs());
        add(JmxUtils.genTestFragment(keyword.getName()));
    }

    /***
     * vistor遇到ifassert时，首先进行断言，并把断言结果存入__lastAssert变量，
     * 然后把__lastAssert变量作为if_true是否执行的判断条件
     */
    @Override
    public void visit(IfAssert ifAssert) {
        String condition = JmxUtils.joinAssertion(ifAssert.getAssertions(), false, assertCnt);
        add(JmxUtils.genJSR223Sampler("if assert", condition, false)).end();
        add(JmxUtils.genSwitchController("if", replaceVar("${__lastAssert" + assertCnt + "}")));
        assertCnt++;
    }

    /***
     * vistor遇到loopinrange，首先需要设置起始下标，对应规范loopinrange中的beg，通过jmeter的jsr223组件实现
     * 为了防止嵌套loopinrange无法区分下标的情况，需要根据嵌套的层级为下标加上前缀，嵌套越深前缀越长，
     * loopinrange截止下标为规范loopinrange中的end，当下标超过end时退出循环，
     * 如果json中没有设置index即下标名，则默认为index，
     * 当用户需要再loopinrange中获取下标时，该值需要设置并每层下标名不能重复，
     * loopinrange中的step执行完一遍后需要跟新下标值，步长为规范loopinrange中的skip
     */
    @Override
    public void visit(LoopInRange loopInRange) {
        String index = loopInRange.getIndexName();
        if (index == null || index.trim().isEmpty()) {
            index = "index";
        } else {
            index = index.trim();
        }
        String iIndex = "__" + index + loopCnt;
        loopCnt++;
        String initScript = JmxUtils.stringFormat("vars.putObject(%s, %d);\n", iIndex, loopInRange.getBeg());
        String postScript = JmxUtils.stringFormat("vars.putObject(%s, vars.getObject(%s));\n" +
                        "vars.putObject(%s, vars.getObject(%s) + %d);\n",
                index, iIndex, iIndex, iIndex, loopInRange.getSkip());
        String condition = JmxUtils.stringFormat("${__groovy(vars.getObject(%s)<%d)}", iIndex, loopInRange.getEnd());
        add(JmxUtils.genJSR223Sampler("init loop", initScript, true)).end();
        add(JmxUtils.genLoopController(condition));
        add(JmxUtils.genJSR223Sampler("exec skip", postScript, true)).end();
    }

    @Override
    public void visit(Stage stage) {
        add(JmxUtils.genGenericController(stage.getStageName()));
    }

    @Override
    public void visit(Testcase testcase) {
        add(JmxUtils.genThreadGroup(testcase.getName(), "1"));
        if (dataset != null) {
            StringJoiner joiner = new StringJoiner(",");
            for (Param param : testcase.getInputs()) {
                joiner.add(param.getName());
            }
            add(JmxUtils.genCSVDataSet(joiner.toString())).end();
        }
    }

    @Override
    public void visit(Testsuite testsuite) {
        testPlanName = testsuite.getName();
        dataset = testsuite.getDependency().getDataset();
        TestPlan testPlan = JmxUtils.genTestPlan(testsuite.getName());
        testPlan.setUserDefinedVariables(JmxUtils.genArguments());
        testPlan.setTestPlanClasspath("");
        add(testPlan);
    }

    /***
     * 每个visible完成遍历后需要调用该方法，
     * 把自身的父节点这是为当前组件
     */
    @Override
    public void postVisit() {
        end();
    }

}
