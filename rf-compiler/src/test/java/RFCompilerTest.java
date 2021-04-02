import com.fasterxml.jackson.databind.ObjectMapper;
import com.srdcloud.taas.compiler.rf.domain.application.RfCompiler;
import com.srdcloud.taas.compiler.rf.domain.entity.TestSuit;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest

public class RFCompilerTest {
    @Test
    public void compile() throws IOException {
        String cp="src\\test\\resources\\";
        ObjectMapper om = new ObjectMapper();
        TestSuit ts = om.readValue(new File(cp + "test.json"), TestSuit.class);
        RfCompiler rfc = new RfCompiler();

        rfc.compile(ts, "output.robot");
    }
}
