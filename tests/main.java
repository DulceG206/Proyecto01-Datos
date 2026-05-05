import java.util.*;
public class Main {
    public static void main(String[] args) {

        boolean trace = true; // activar trace
        ScriptInterpreter interpreter = new ScriptInterpreter(trace);

        List<String> script = Arrays.asList(
                "Valid SignaturemyPublicKey",  // firma que de que sí pasa el verifySignature
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
}
