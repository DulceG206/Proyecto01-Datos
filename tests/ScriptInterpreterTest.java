import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ScriptInterpreterTest {

    @Test
    public void testP2PKHCorrecto() {
        ScriptInterpreter si = new ScriptInterpreter();

        List<String> script = Arrays.asList(
                "Valid SignaturemyPublicKey",
                "myPublicKey",
                "OP_DUP",
                "OP_HASH160",
                CryptoUtils.hash160("myPublicKey"),
                "OP_EQUALVERIFY",
                "OP_CHECKSIG"
        );

        assertTrue(si.execute(script));
    }

    @Test
    public void testP2PKHIncorrecto() {
        ScriptInterpreter si = new ScriptInterpreter();

        List<String> script = Arrays.asList(
                "badSignature",
                "myPublicKey",
                "OP_DUP",
                "OP_HASH160",
                CryptoUtils.hash160("myPublicKey"),
                "OP_EQUALVERIFY",
                "OP_CHECKSIG"
        );

        assertFalse(si.execute(script));
    }

    @Test
    public void testIfElse() {
        ScriptInterpreter si = new ScriptInterpreter();

        List<String> script = Arrays.asList(
                "TRUE",
                "OP_IF",
                "TRUE",
                "OP_ELSE",
                "FALSE",
                "OP_ENDIF"
        );

        assertTrue(si.execute(script));
    }

    @Test
    public void testStackUnderflow() {
        ScriptInterpreter si = new ScriptInterpreter();

        List<String> script = Arrays.asList(
                "OP_DROP"
        );

        assertFalse(si.execute(script));
    }
}