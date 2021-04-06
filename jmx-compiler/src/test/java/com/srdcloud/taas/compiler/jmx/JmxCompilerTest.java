package com.srdcloud.taas.compiler.jmx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srdcloud.taas.common.domain.compile.entity.Testsuite;
import com.srdcloud.taas.compiler.jmx.infra.jmeterutil.JmxUtils;
import com.srdcloud.taas.compiler.jmx.application.JmxVisitor;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@SpringBootTest
public class JmxCompilerTest {

    @Test
    public void compile() throws IOException {
        URL u = JmxCompilerTest.class.getClassLoader().getResource("");
        String cp = u.getPath();
        ObjectMapper om = new ObjectMapper();
        Testsuite ts = om.readValue(new File(cp + "test.json"), Testsuite.class);
        JmxVisitor jv = new JmxVisitor();
        ts.accept(jv);
        String testFile = cp + "test.jmx";
        JmxUtils.save(jv.getHashTree(), testFile);
    }
}
