package com.srdcloud.taas.compiler.jmx.infra.jmeterutil;

import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.assertions.gui.AssertionGui;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.CSVDataSet;
import org.apache.jmeter.config.CSVDataSetBeanInfo;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.*;
import org.apache.jmeter.control.gui.*;
import org.apache.jmeter.extractor.JSR223PostProcessor;
import org.apache.jmeter.protocol.http.config.gui.HttpDefaultsGui;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.HTTPArgumentsPanel;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.protocol.java.sampler.JSR223Sampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.util.JSR223TestElement;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JmxUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JmxUtils.class);
    private static volatile boolean INIT_FLAG = false;

    /***
     * 初始化生成jmx文件时需要的jmeter属性
     */
    public static void init() {
        if(!INIT_FLAG) {
            synchronized (JmxUtils.class) {
                if(!INIT_FLAG) {
                    URL u = JmxUtils.class.getClassLoader().getResource("");
                    String cp = u.getPath();
                    LOG.info("init jmeter properties start, classpath {}", cp);
                    JMeterUtils.setJMeterHome(new File(cp).getAbsolutePath());
                    JMeterUtils.loadJMeterProperties(new File(cp + "jmeter.properties").getAbsolutePath());
                    JMeterUtils.setProperty("saveservice_properties", cp + "saveservice.properties");
                    INIT_FLAG = true;
                    LOG.info("init jmeter properties finished");
                }
            }
        }
    }

    public static void save(HashTree hashTree, String jmxPath) throws IOException {
        JmxUtils.init();
        SaveService.saveTree(hashTree, new FileOutputStream(jmxPath));
    }

    /***
     * 过滤字符串中的非字母和非数字字符
     * @param str 需要过滤的字符串
     * @return 过滤后的字符串
     */
    public static String filterNotAlpha(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /***
     * 格式化字符串并把args中的字符串的'替换为\'，防止脚本产生语法错误
     * @param format 字符串格式化模板
     * @param args 格式化参数
     * @return 格式化后的字符串
     */
    public static String stringFormat(String format, Object... args) {
        Object[] tArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                tArgs[i] = FunctionParser.replaceVar(args[i].toString());
            } else if (args[i] instanceof TextNode) {
                tArgs[i] = ((TextNode) args[i]).asText();
            } else {
                tArgs[i] = args[i];
            }
        }
        return String.format(format, tArgs);
    }

    /***
     * 判断字符串是否为数字
     * @param o 需要判断的字符串
     * @return 是字符串则返回true
     */
    public static boolean isStrNumeric(Object o) {
        if (!(o instanceof String)) {
            return false;
        }
        String str = (String) o;
        if ("".equals(str)) {
            return false;
        }
        boolean dot = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i == 0) {
                if (c != '-' && c != '+' && (c < '0' || c > '9') && c != '.') {
                    return false;
                }
            } else if (c == '.') {
                if (dot) {
                    return false;
                }
                dot = true;
            } else if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /***
     * 构造断言脚本片段
     * @param assertions 断言的条件，list中包含一个map，map中包含的key有field、operator、value，
     *                   三个key的value拼接后的条件如${username}==taas，条件之间通过&&连接
     * @param isFieldRef field是否为脚本中的变量，若是则不需要使用引号包含
     * @return 构造后的断言片段
     */
    public static String joinAssertion(List<Map<String, String>> assertions, boolean isFieldRef, int assertCnt) {
        StringBuilder sb = new StringBuilder();
        StringBuilder psb = new StringBuilder();
        StringBuilder msgSb = new StringBuilder();
        for (int i = 0; i < assertions.size(); i++) {
            Map<String, String> m = assertions.get(i);
            Object field = m.get("field");
            if(isFieldRef) {
                field = new TextNode(field.toString());
            }
            TextNode operator = new TextNode(m.get("operator"));
            Object value = m.get("value");
            if(isStrNumeric(value)) {
                value = new TextNode(value.toString());
            }
            String tmpMsg = stringFormat("%s %s %s", field, operator, value);
            if ("regex".equals(operator.asText())) {
                String tmp = "Pattern pattern%s = JMeterUtils.getPatternCache().getPattern(%s);\n";
                psb.append(stringFormat(tmp, i, m.get("value")));
                tmp = "JMeterUtils.getMatcher().matches(%s, pattern%s)";
                sb.append(stringFormat(tmp, field, i));
            } else {
                sb.append(tmpMsg);
            }
            msgSb.append(tmpMsg);
            if (i + 1 != assertions.size()) {
                sb.append(" && ");
                msgSb.append(" && ");
            }
        }
        String tmp = "if(%s){\n" +
                "SampleResult.setResponseMessage(%s);\n" +
                "SampleResult.setSuccessful(true);\n" +
                "vars.putObject('__lastAssert%d', true);\n" +
                "} else {\n" +
                "SampleResult.setResponseMessage(%s);\n" +
                "SampleResult.setSuccessful(false);\n" +
                "vars.putObject('__lastAssert%d', false);\n" +
                "}\n";
        String succeededStr = stringFormat("%s", msgSb + " assert succeeded");
        String failedStr = stringFormat("%s", msgSb + " assert failed");
        tmp = String.format(tmp, sb, succeededStr, assertCnt, failedStr, assertCnt);
        if (psb.length() > 0) {
            return "import org.apache.jmeter.util.JMeterUtils;\n" +
                    "import org.apache.oro.text.regex.Pattern;\n" +
                    psb.toString() + tmp;
        }
        return tmp;
    }

    /***
     * 构造http请求中的header
     * @param mHeaders map中的key和value分别对应header中的key和value
     * @return jmeter中的header组件
     */
    public static HeaderManager genHeaderManager(Map<String, String> mHeaders) {
        HeaderManager headerManager = new HeaderManager();
        headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
        headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        headerManager.setName("HTTP Header Manager");
        headerManager.setEnabled(true);
        for (Map.Entry<String, String> en : mHeaders.entrySet()) {
            Header header = new Header();
            header.setName(en.getKey());
            header.setValue(en.getValue());
            headerManager.add(header);
        }
        return headerManager;
    }

    /***
     * 通过jmeter中的fragment组件实现自定义关键字
     * @param testFragmentName 自定义关键字名
     * @return jmeter中的fragment组件
     */
    public static TestFragmentController genTestFragment(String testFragmentName) {
        TestFragmentController testFragmentController = new TestFragmentController();
        testFragmentController.setName(testFragmentName);
        testFragmentController.setEnabled(false);
        testFragmentController.setProperty(TestElement.GUI_CLASS, TestFragmentControllerGui.class.getName());
        testFragmentController.setProperty(TestElement.TEST_CLASS, TestFragmentController.class.getName());
        return testFragmentController;
    }

    /***
     * 通过jmeter中的module组件实现自定义关键字调用
     * @param moduleControllerName module自定义名
     * @param testPlanName 对应规范中testsuite名
     * @param testFragmentName 自定义关键字名
     * @return jmeter中的module组件
     */
    public static ModuleController genModuleController(String moduleControllerName, String testPlanName, String testFragmentName) {
        ModuleController moduleController = new ModuleController();
        moduleController.setName(moduleControllerName);
        moduleController.setProperty(TestElement.GUI_CLASS, ModuleControllerGui.class.getName());
        moduleController.setProperty(TestElement.TEST_CLASS, ModuleController.class.getName());
        List<String> nodePath = new ArrayList<>();
        nodePath.add("Test Plan");
        nodePath.add(testPlanName);
        nodePath.add(testFragmentName);
        moduleController.setProperty(new CollectionProperty("ModuleController.node_path", nodePath));
        return moduleController;
    }

    /***
     * 通过jmeter中的testplan组件构造testsuite
     * @param testPlanName 对应规范中testsuite名
     * @return jmeter中的testplan组件
     */
    public static TestPlan genTestPlan(String testPlanName) {
        TestPlan testPlan = new TestPlan(testPlanName);
        testPlan.setComment("");
        testPlan.setFunctionalMode(false);
        testPlan.setTearDownOnShutdown(true);
        testPlan.setSerialized(false);
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
        testPlan.setEnabled(true);
        return testPlan;
    }

    /***
     * 当其他jmeter组件需要的时候，构造jmeter中的arguments组件
     * @return jmeter中的arguments组件
     */
    public static Arguments genArguments() {
        return genArguments(null);
    }

    /***
     * 当其他jmeter组件需要的时候，构造jmeter中的arguments组件
     * @param guiClass 组件的TestElement.gui_class属性
     * @return jmeter中的arguments组件
     */
    public static Arguments genArguments(String guiClass) {
        Arguments arguments = new Arguments();
        if (guiClass == null) {
            arguments.setProperty(TestElement.GUI_CLASS, ArgumentsPanel.class.getName());
        } else {
            arguments.setProperty(TestElement.GUI_CLASS, HTTPArgumentsPanel.class.getName());
        }
        arguments.setProperty(TestElement.TEST_CLASS, Arguments.class.getName());
        arguments.setName("User Defined Variables");
        arguments.setEnabled(true);
        return arguments;
    }

    /***
     * 通过jmeter中的threadgroup组件构造testcase
     * @param ThreadGroupName 规范中的testcase名
     * @param threadCnt 组件的并发数，可模拟不同用户
     * @return jmeter中的threadgroup组件
     */
    public static ThreadGroup genThreadGroup(String ThreadGroupName, String threadCnt) {
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName(ThreadGroupName);
        threadGroup.setEnabled(true);
        threadGroup.setProperty(AbstractThreadGroup.ON_SAMPLE_ERROR, AbstractThreadGroup.ON_SAMPLE_ERROR_CONTINUE);
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());

        LoopController loopController = new LoopController();
        loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setName("Loop Controller");
        loopController.setLoops("1");
        loopController.setEnabled(true);

        threadGroup.setSamplerController(loopController);
        threadGroup.setProperty(ThreadGroup.NUM_THREADS, threadCnt);
        threadGroup.setProperty(ThreadGroup.RAMP_TIME, "1");
        threadGroup.setScheduler(false);
        threadGroup.setProperty(ThreadGroup.DURATION, "");
        threadGroup.setProperty(ThreadGroup.DELAY, "");
        threadGroup.setIsSameUserOnNextIteration(true);

        return threadGroup;
    }

    public static OnceOnlyController genOnceOnlyController() {
        OnceOnlyController onceOnlyController = new OnceOnlyController();
        onceOnlyController.setProperty(TestElement.GUI_CLASS, OnceOnlyControllerGui.class.getName());
        onceOnlyController.setProperty(TestElement.TEST_CLASS, OnceOnlyController.class.getName());
        onceOnlyController.setName("Once Only Controller");
        onceOnlyController.setEnabled(true);
        return onceOnlyController;
    }

    /***
     * 通过jmeter中的while组件构造loopinrange step
     * @param condition condition为真则执行loopinrange中定义的step
     * @return jmeter中的while组件
     */
    public static WhileController genLoopController(String condition) {
        WhileController whileController = new WhileController();
        whileController.setProperty(TestElement.GUI_CLASS, WhileControllerGui.class.getName());
        whileController.setProperty(TestElement.TEST_CLASS, WhileController.class.getName());
        whileController.setName("While Controller");
        whileController.setEnabled(true);
        whileController.setCondition(condition);
        return whileController;
    }

    /***
     * 通过jmeter中的switch组件构造ifelse step
     * @param selectStr selectStr取值为true或false，分别执行不同分支
     * @return jmeter中的while组件
     */
    public static SwitchController genSwitchController(String name, String selectStr) {
        SwitchController switchController = new SwitchController();
        switchController.setProperty(TestElement.GUI_CLASS, SwitchControllerGui.class.getName());
        switchController.setProperty(TestElement.TEST_CLASS, SwitchController.class.getName());
        switchController.setName(name);
        switchController.setEnabled(true);
        switchController.setSelection(selectStr);
        return switchController;
    }


    /***
     * 通过jmeter中的generic组件构造stage step
     * @param stageName stage自定义名
     * @return jmeter中的generic组件
     */
    public static GenericController genGenericController(String stageName) {
        GenericController genericController = new GenericController();
        genericController.setName(stageName);
        genericController.setProperty(TestElement.GUI_CLASS, LogicControllerGui.class.getName());
        genericController.setProperty(TestElement.TEST_CLASS, GenericController.class.getName());
        genericController.setEnabled(true);
        return genericController;
    }

    public static HTTPSamplerProxy genHTTPSampleProxy(String api) {
        return genHTTPSampleProxy(api, HTTPConstants.GET, null);
    }

    /***
     * 通过jmeter中的http组件构造http step
     * @param api http请求路径
     * @param method http请求方法
     * @param arguments jmeter中http组件可选参数
     * @return jmeter中的http组件
     */
    public static HTTPSamplerProxy genHTTPSampleProxy(String api, String method, Arguments arguments) {
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
        httpSamplerProxy.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSamplerProxy.setName("HTTP Request");
        httpSamplerProxy.setEnabled(true);

        if (arguments == null) {
            arguments = genArguments(HTTPArgumentsPanel.class.getName());
        } else {
            httpSamplerProxy.removeProperty(HTTPSamplerBase.ARGUMENTS);
        }

        if (HTTPConstants.POST.equals(method)) {
            httpSamplerProxy.setPostBodyRaw(true);
        }

        httpSamplerProxy.setArguments(arguments);
        httpSamplerProxy.setDomain("${__P(domain,)}");
        httpSamplerProxy.setProperty(HTTPSamplerBase.PORT, "${__P(port,)}");
        httpSamplerProxy.setProtocol("");
        httpSamplerProxy.setContentEncoding("");
        httpSamplerProxy.setPath(api);
        httpSamplerProxy.setMethod(method);
        httpSamplerProxy.setFollowRedirects(true);
        httpSamplerProxy.setAutoRedirects(false);
        httpSamplerProxy.setUseKeepAlive(true);
        httpSamplerProxy.setDoMultipart(false);
        httpSamplerProxy.setEmbeddedUrlRE("");
        httpSamplerProxy.setProperty(HTTPSamplerProxy.CONNECT_TIMEOUT, "");
        httpSamplerProxy.setProperty(HTTPSamplerProxy.RESPONSE_TIMEOUT, "");
        return httpSamplerProxy;
    }

    public static void genJSR223TestElement(String name, String script, boolean ignore, JSR223TestElement jsr223TestElement) {
        jsr223TestElement.setProperty(TestElement.GUI_CLASS, TestBeanGUI.class.getName());
        jsr223TestElement.setProperty(TestElement.TEST_CLASS, jsr223TestElement.getClass().getName());
        jsr223TestElement.setName(name);
        jsr223TestElement.setEnabled(true);
        jsr223TestElement.setProperty("scriptLanguage", "groovy");
        jsr223TestElement.setProperty("parameters", "");
        jsr223TestElement.setProperty("filename", "");
        jsr223TestElement.setProperty("cacheKey", false);
        if (ignore) {
            script += "SampleResult.setIgnore();";
        }
        jsr223TestElement.setProperty("script", script);
    }

    /***
     * 通过jmeter中的jsr223组件实现规范中的部分内建关键字
     * @param name 自定义组件名
     * @param script groovy脚本
     * @param ignore 为真则jmeter改组件调用记录不会出现在sampleresult收集中
     * @return jmeter中的jsr223组件
     */
    public static JSR223Sampler genJSR223Sampler(String name, String script, boolean ignore) {
        JSR223Sampler jsr223Sampler = new JSR223Sampler();
        genJSR223TestElement(name, script, ignore, jsr223Sampler);
        return jsr223Sampler;
    }

    public static JSR223PostProcessor genJSR223PostProcessor(String name, String script, boolean ignore) {
        JSR223PostProcessor jsr223PostProcessor = new JSR223PostProcessor();
        genJSR223TestElement(name, script, ignore, jsr223PostProcessor);
        return jsr223PostProcessor;
    }

    /***
     * 通过jmeter中的csv组件实现规范中的dataset
     * @param variableNames 规范中的input变量名
     * @return jmeter中的csv组件
     */
    public static CSVDataSet genCSVDataSet(String variableNames) {
        CSVDataSet csvDataSet = new CSVDataSet();
        csvDataSet.setProperty(TestElement.GUI_CLASS, TestBeanGUI.class.getName());
        csvDataSet.setProperty(TestElement.TEST_CLASS, CSVDataSet.class.getName());
        csvDataSet.setName("CSV Data Set Config");
        csvDataSet.setEnabled(true);
        csvDataSet.setProperty("filename", "${__P(csvfile,)}");
        csvDataSet.setProperty("fileEncoding", "");
        csvDataSet.setProperty("variableNames", variableNames);
        csvDataSet.setProperty("ignoreFirstLine", false);
        csvDataSet.setProperty("delimiter", ",");
        csvDataSet.setProperty("quotedData", false);
        csvDataSet.setProperty("recycle", false);
        csvDataSet.setProperty("stopThread", true);
        csvDataSet.setProperty("shareMode", CSVDataSetBeanInfo.getShareTags()[0]);
        return csvDataSet;
    }

    public static ConfigTestElement genConfigTestElement(String ip, String port) {
        ConfigTestElement configTestElement = new ConfigTestElement();
        configTestElement.setProperty(TestElement.GUI_CLASS, HttpDefaultsGui.class.getName());
        configTestElement.setProperty(TestElement.TEST_CLASS, ConfigTestElement.class.getName());
        configTestElement.setEnabled(true);
        configTestElement.setName("HTTP Request Defaults");
        Arguments arguments = genArguments(HTTPArgumentsPanel.class.getName());
        configTestElement.setProperty(new TestElementProperty(HTTPSamplerBase.ARGUMENTS, arguments));
        configTestElement.setProperty(HTTPSamplerBase.DOMAIN, ip);
        configTestElement.setProperty(HTTPSamplerBase.PORT, port);
        configTestElement.setProperty(HTTPSamplerBase.PROTOCOL, "");
        configTestElement.setProperty(HTTPSamplerBase.CONTENT_ENCODING, "");
        configTestElement.setProperty(HTTPSamplerBase.PATH, "");
        configTestElement.setProperty(HTTPSamplerBase.CONCURRENT_POOL, "6");
        configTestElement.setProperty(HTTPSamplerBase.CONNECT_TIMEOUT, "");
        configTestElement.setProperty(HTTPSamplerBase.RESPONSE_TIMEOUT, "");
        return configTestElement;
    }

    public static ResponseAssertion genResponseAssertion(String code) {
        ResponseAssertion responseAssertion = new ResponseAssertion();
        responseAssertion.setProperty(TestElement.GUI_CLASS, AssertionGui.class.getName());
        responseAssertion.setProperty(TestElement.TEST_CLASS, ResponseAssertion.class.getName());
        responseAssertion.setName("Response Assertion");
        responseAssertion.setEnabled(true);
        responseAssertion.getTestStrings().addItem(code);
        responseAssertion.setCustomFailureMessage("");
        responseAssertion.setTestFieldResponseCode();
        responseAssertion.setAssumeSuccess(false);
        responseAssertion.setToEqualsType();
        return responseAssertion;
    }

    public static ResultCollector genResultCollector(String guiClass, String name) {
        ResultCollector resultCollector = new ResultCollector();
        resultCollector.setProperty(TestElement.GUI_CLASS, guiClass);
        resultCollector.setProperty(TestElement.TEST_CLASS, ResultCollector.class.getName());
        resultCollector.setName(name);
        resultCollector.setEnabled(true);
        resultCollector.setErrorLogging(false);
        SampleSaveConfiguration sampleSaveConfiguration = new SampleSaveConfiguration();
        resultCollector.setSaveConfig(sampleSaveConfiguration);
        resultCollector.setFilename("");
        return resultCollector;
    }
}
