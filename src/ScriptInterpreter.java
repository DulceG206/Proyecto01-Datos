import java.util.*;

public class ScriptInterpreter {

    private Deque<String> stack = new ArrayDeque<>();
    private Map<String, Runnable> opcodes = new HashMap<>();

    public ScriptInterpreter() {
        registerOpcodes();
    }

    private void registerOpcodes() {

        opcodes.put("OP_DUP", () -> {
            String top = stack.peek();
            stack.push(top);
        });

        opcodes.put("OP_DROP", () -> {
            stack.pop();
        });

        opcodes.put("OP_EQUAL", () -> {
            String a = stack.pop();
            String b = stack.pop();
            stack.push(a.equals(b) ? "TRUE" : "FALSE");
        });

        opcodes.put("OP_EQUALVERIFY", () -> {
            String a = stack.pop();
            String b = stack.pop();
            if (!a.equals(b)) {
                throw new RuntimeException("Verification failed");
            }
        });

        opcodes.put("OP_HASH160", () -> {
            String value = stack.pop();
            stack.push(CryptoUtils.hash160(value));
        });

        opcodes.put("OP_CHECKSIG", () -> {
            String pubKey = stack.pop();
            String signature = stack.pop();
            boolean valid = CryptoUtils.verifySignature(signature, pubKey);
            stack.push(valid ? "TRUE" : "FALSE");
        });
    }

    public boolean execute(List<String> script) {
        for (String instruction : script) {

            if (opcodes.containsKey(instruction)) {
                opcodes.get(instruction).run();
            } else {
                stack.push(instruction);
            }
        }

        return stack.size() == 1 && stack.pop().equals("TRUE");
    }
}