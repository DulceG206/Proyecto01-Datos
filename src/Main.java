import java.util.*;

public class Main {
    public static void main(String[] args) {

        ScriptInterpreter interpreter = new ScriptInterpreter();

        List<String> script = Arrays.asList(
                "validSignature",
                "myPublicKey",
                "OP_DUP",
                "OP_HASH160",
                CryptoUtils.hash160("myPublicKey"),
                "OP_EQUALVERIFY",
                "OP_CHECKSIG"
        );

        boolean result = interpreter.execute(script);
        System.out.println("Resultado: " + result);
    }

    private static class ScriptInterpreter {

        public ScriptInterpreter() {
        }

        private boolean execute(List<String> script) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private static class CryptoUtils {

        public CryptoUtils() {
        }
    }
}