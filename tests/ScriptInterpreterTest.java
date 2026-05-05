import java.util.*;

public class ScriptInterpreter {

    private Deque<String> stack = new ArrayDeque<>();
    private Map<String, Runnable> opcodes = new HashMap<>();
    private boolean trace = false;

    public ScriptInterpreter(boolean trace) {
        this.trace = trace;
        registerOpcodes();
    }

    public ScriptInterpreter() {
        this(false);
    }

    private void log(String msg) {
        if (trace) {
            System.out.println(msg + " | Stack: " + stack);
        }
    }

    private void checkStack(int size) {
        if (stack.size() < size) {
            throw new RuntimeException("Stack underflow");
        }
    }

    private void registerOpcodes() {

        opcodes.put("OP_DUP", () -> {
            checkStack(1);
            String top = stack.peek();
            stack.push(top);
            log("OP_DUP");
        });

        opcodes.put("OP_DROP", () -> {
            checkStack(1);
            stack.pop();
            log("OP_DROP");
        });

        opcodes.put("OP_EQUAL", () -> {
            checkStack(2);
            String a = stack.pop();
            String b = stack.pop();
            stack.push(a.equals(b) ? "TRUE" : "FALSE");
            log("OP_EQUAL");
        });

        opcodes.put("OP_EQUALVERIFY", () -> {
            checkStack(2);
            String a = stack.pop();
            String b = stack.pop();
            if (!a.equals(b)) {
                throw new RuntimeException("Verification failed");
            }
            log("OP_EQUALVERIFY");
        });

        opcodes.put("OP_HASH160", () -> {
            checkStack(1);
            String value = stack.pop();
            stack.push(CryptoUtils.hash160(value));
            log("OP_HASH160");
        });

        opcodes.put("OP_CHECKSIG", () -> {
            checkStack(2);
            String pubKey = stack.pop();
            String signature = stack.pop();
            boolean valid = CryptoUtils.verifySignature(signature, pubKey);
            stack.push(valid ? "TRUE" : "FALSE");
            log("OP_CHECKSIG");
        });

    
        opcodes.put("OP_IF", () -> handleIf(true));
        opcodes.put("OP_ELSE", () -> handleIf(false));
        opcodes.put("OP_ENDIF", () -> {});
    }

    // Manejo básico de IF/ELSE
    private boolean executing = true;
    private boolean lastCondition = false;

    private void handleIf(boolean isIf) {
        checkStack(1);
        String condition = stack.pop();
        boolean cond = condition.equals("TRUE");

        if (isIf) {
            lastCondition = cond;
            executing = cond;
        } else {
            executing = !lastCondition;
        }
    }

    public boolean execute(List<String> script) {
        try {
            for (String instruction : script) {

                if (opcodes.containsKey(instruction)) {
                    opcodes.get(instruction).run();
                } else {
                    if (executing) {
                        stack.push(instruction);
                        log("PUSH " + instruction);
                    }
                }
            }

            if (stack.isEmpty()) return false;

            return stack.pop().equals("TRUE");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}