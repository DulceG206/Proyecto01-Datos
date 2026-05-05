import java.util.*;
public class ScriptInterpreter {

    //campos

    //Pila principal de operandos.
    private Deque<String> stack = new ArrayDeque<>();

    //Mapa de opcodes registrados como lambdas
    private Map<String, Runnable> opcodes = new HashMap<>();

    //mprime el estado de la pila tras cada instrucción.
    private boolean trace = false;

    // estado para condicionales
    private boolean executing     = true;
    private boolean lastCondition = false;

    // --------- constructores  ---------

     * @param trace si es {@code true} imprime la pila tras cada instrucción.
    public ScriptInterpreter(boolean trace) {
        this.trace = trace;
        registerOpcodes();
    }

    // Crea el intérprete sin modo trace.
    public ScriptInterpreter() {
        this(false);
    }

    // métodos internos

    /**
     * Imprime el mensaje y el estado actual de la pila si trace está activo.
     * @param msg nombre de la instrucción ejecutada.
     */
    private void log(String msg) {
        if (trace) {
            System.out.println(msg + " | Stack: " + stack);
        }
    }

    /**
     * Verifica que la pila tenga al menos {@code size} elementos.
     * @param size cantidad mínima requerida.
     * @throws RuntimeException si hay menos elementos que los requeridos.
     */
    private void checkStack(int size) {
        if (stack.size() < size) {
            throw new RuntimeException("Stack underflow");
        }
    }

    /**
     * Interpreta un valor de la pila como booleano.
     * "TRUE" y cualquier número distinto de 0 son verdaderos.
     */
    private boolean isTruthy(String value) {
        if (value.equals("TRUE"))  return true;
        if (value.equals("FALSE")) return false;
        try { return Integer.parseInt(value) != 0; }
        catch (NumberFormatException e) { return !value.isEmpty(); }
    }

    /**
     * Interpreta un valor de la pila como entero.
     * @throws RuntimeException si el valor no es numérico.
     */
    private int toInt(String value) {
        try { return Integer.parseInt(value); }
        catch (NumberFormatException e) {
            throw new RuntimeException("Se esperaba un número, se obtuvo: " + value);
        }
    }

    // registro de opcodes

    private void registerOpcodes() {

        //  PILA

        opcodes.put("OP_DUP", () -> {
            checkStack(1);
            stack.push(stack.peek());
            log("OP_DUP");
        });

        opcodes.put("OP_DROP", () -> {
            checkStack(1);
            stack.pop();
            log("OP_DROP");
        });

        opcodes.put("OP_SWAP", () -> {
            checkStack(2);
            String a = stack.pop();
            String b = stack.pop();
            stack.push(a);
            stack.push(b);
            log("OP_SWAP");
        });

        opcodes.put("OP_OVER", () -> {
            checkStack(2);
            String a = stack.pop();
            String b = stack.peek();
            stack.push(a);
            stack.push(b);
            log("OP_OVER");
        });

        //  LITERALES 

        opcodes.put("OP_0",     () -> { stack.push("0");     log("OP_0");     });
        opcodes.put("OP_FALSE", () -> { stack.push("FALSE"); log("OP_FALSE"); });
        opcodes.put("OP_1",     () -> { stack.push("1");     log("OP_1");     });
        opcodes.put("OP_TRUE",  () -> { stack.push("TRUE");  log("OP_TRUE");  });
        opcodes.put("OP_2",     () -> { stack.push("2");     log("OP_2");     });
        opcodes.put("OP_3",     () -> { stack.push("3");     log("OP_3");     });
        opcodes.put("OP_4",     () -> { stack.push("4");     log("OP_4");     });
        opcodes.put("OP_5",     () -> { stack.push("5");     log("OP_5");     });
        opcodes.put("OP_6",     () -> { stack.push("6");     log("OP_6");     });
        opcodes.put("OP_7",     () -> { stack.push("7");     log("OP_7");     });
        opcodes.put("OP_8",     () -> { stack.push("8");     log("OP_8");     });
        opcodes.put("OP_9",     () -> { stack.push("9");     log("OP_9");     });
        opcodes.put("OP_10",    () -> { stack.push("10");    log("OP_10");    });
        opcodes.put("OP_11",    () -> { stack.push("11");    log("OP_11");    });
        opcodes.put("OP_12",    () -> { stack.push("12");    log("OP_12");    });
        opcodes.put("OP_13",    () -> { stack.push("13");    log("OP_13");    });
        opcodes.put("OP_14",    () -> { stack.push("14");    log("OP_14");    });
        opcodes.put("OP_15",    () -> { stack.push("15");    log("OP_15");    });
        opcodes.put("OP_16",    () -> { stack.push("16");    log("OP_16");    });

        //  LÓGICA Y COMPARACIÓN 

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
            if (!a.equals(b)) throw new RuntimeException("OP_EQUALVERIFY: " + a + " != " + b);
            log("OP_EQUALVERIFY");
        });

        opcodes.put("OP_NOT", () -> {
            checkStack(1);
            stack.push(isTruthy(stack.pop()) ? "FALSE" : "TRUE");
            log("OP_NOT");
        });

        opcodes.put("OP_BOOLAND", () -> {
            checkStack(2);
            String a = stack.pop();
            String b = stack.pop();
            stack.push((isTruthy(a) && isTruthy(b)) ? "TRUE" : "FALSE");
            log("OP_BOOLAND");
        });

        opcodes.put("OP_BOOLOR", () -> {
            checkStack(2);
            String a = stack.pop();
            String b = stack.pop();
            stack.push((isTruthy(a) || isTruthy(b)) ? "TRUE" : "FALSE");
            log("OP_BOOLOR");
        });

        //  ARITMÉTICA 

        opcodes.put("OP_ADD", () -> {
            checkStack(2);
            int a = toInt(stack.pop());
            int b = toInt(stack.pop());
            stack.push(String.valueOf(a + b));
            log("OP_ADD");
        });

        opcodes.put("OP_SUB", () -> {
            checkStack(2);
            int a = toInt(stack.pop());
            int b = toInt(stack.pop());
            stack.push(String.valueOf(b - a));
            log("OP_SUB");
        });

        opcodes.put("OP_NUMEQUALVERIFY", () -> {
            checkStack(2);
            int a = toInt(stack.pop());
            int b = toInt(stack.pop());
            if (a != b) throw new RuntimeException("OP_NUMEQUALVERIFY: " + a + " != " + b);
            log("OP_NUMEQUALVERIFY");
        });

        opcodes.put("OP_LESSTHAN", () -> {
            checkStack(2);
            int a = toInt(stack.pop());
            int b = toInt(stack.pop());
            stack.push(b < a ? "TRUE" : "FALSE");
            log("OP_LESSTHAN");
        });

        opcodes.put("OP_GREATERTHAN", () -> {
            checkStack(2);
            int a = toInt(stack.pop());
            int b = toInt(stack.pop());
            stack.push(b > a ? "TRUE" : "FALSE");
            log("OP_GREATERTHAN");
        });

        opcodes.put("OP_LESSTHANOREQUAL", () -> {
            checkStack(2);
            int a = toInt(stack.pop());
            int b = toInt(stack.pop());
            stack.push(b <= a ? "TRUE" : "FALSE");
            log("OP_LESSTHANOREQUAL");
        });

        opcodes.put("OP_GREATERTHANOREQUAL", () -> {
            checkStack(2);
            int a = toInt(stack.pop());
            int b = toInt(stack.pop());
            stack.push(b >= a ? "TRUE" : "FALSE");
            log("OP_GREATERTHANOREQUAL");
        });

        // CRIPTOGRAFÍA 

        opcodes.put("OP_SHA256", () -> {
            checkStack(1);
            stack.push(CryptoUtils.sha256(stack.pop()));
            log("OP_SHA256");
        });

        opcodes.put("OP_HASH160", () -> {
            checkStack(1);
            stack.push(CryptoUtils.hash160(stack.pop()));
            log("OP_HASH160");
        });

        opcodes.put("OP_HASH256", () -> {
            checkStack(1);
            stack.push(CryptoUtils.hash256(stack.pop()));
            log("OP_HASH256");
        });

        //  FIRMAS 

        opcodes.put("OP_CHECKSIG", () -> {
            checkStack(2);
            String pubKey    = stack.pop();
            String signature = stack.pop();
            stack.push(CryptoUtils.verifySignature(signature, pubKey) ? "TRUE" : "FALSE");
            log("OP_CHECKSIG");
        });

        opcodes.put("OP_CHECKSIGVERIFY", () -> {
            checkStack(2);
            String pubKey    = stack.pop();
            String signature = stack.pop();
            if (!CryptoUtils.verifySignature(signature, pubKey))
                throw new RuntimeException("OP_CHECKSIGVERIFY: firma inválida");
            log("OP_CHECKSIGVERIFY");
        });

        //  CONTROL DE FLUJO 
        opcodes.put("OP_VERIFY", () -> {
            checkStack(1);
            if (!isTruthy(stack.pop()))
                throw new RuntimeException("OP_VERIFY: falló la verificación");
            log("OP_VERIFY");
        });

        opcodes.put("OP_RETURN", () -> {
            throw new RuntimeException("OP_RETURN: script inválido");
        });

        opcodes.put("OP_IF",    () -> handleIf());
        opcodes.put("OP_NOTIF", () -> handleNotIf());
        opcodes.put("OP_ELSE",  () -> handleElse());
        opcodes.put("OP_ENDIF", () -> { executing = true; log("OP_ENDIF"); });
        opcodes.put("OP_NOP",   () -> log("OP_NOP"));
    }

    // condicionales

    // OP_IF: ejecuta el bloque siguiente si el tope es verdadero.
    private void handleIf() {
        checkStack(1);
        boolean cond = isTruthy(stack.pop());
        lastCondition = cond;
        executing     = cond;
        log("OP_IF");
    }

    //OP_NOTIF: ejecuta el bloque siguiente si el tope es falso.
    private void handleNotIf() {
        checkStack(1);
        boolean cond = !isTruthy(stack.pop());
        lastCondition = cond;
        executing     = cond;
        log("OP_NOTIF");
    }

    // OP_ELSE: invierte la rama activa.
    private void handleElse() {
        executing = !lastCondition;
        log("OP_ELSE");
    }

    //  ejecución principal

    /**
     * Ejecuta el script completo scriptSig + scriptPubKey concatenados
     * @param script lista de instrucciones y datos.
     * @return {@code true} si la pila termina con un valor verdadero en la cima.
     */
    public boolean execute(List<String> script) {
        stack.clear();
        executing = true;
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
            return isTruthy(stack.pop());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}