package com.srdcloud.taas.compiler.jmx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srdcloud.taas.common.domain.compile.entity.Testsuite;
import com.srdcloud.taas.compiler.jmx.infra.jmeterutil.JmxUtils;
import com.srdcloud.taas.compiler.jmx.application.JmxVisitor;
import com.srdcloud.taas.common.domain.compile.StepDeser;
import org.apache.jmeter.save.SaveService;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@SpringBootTest
public class JmxCompilerTest {

    @Test
    public void compile() throws IOException {
        URL u = StepDeser.class.getClassLoader().getResource("");
        String cp = u.getPath();
        ObjectMapper om = new ObjectMapper();
        Testsuite ts = om.readValue(new File(cp + "test.json"), Testsuite.class);
        JmxVisitor jv = new JmxVisitor();
        ts.accept(jv);
        String testFile = cp + "test.jmx";
        JmxUtils.init(cp);
        SaveService.saveTree(jv.getHashTree(), new FileOutputStream(testFile));
        System.out.println();
    }
}
