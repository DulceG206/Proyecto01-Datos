import java.security.MessageDigest;

public class CryptoUtils {

    public static String hash160(String input) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256.digest(input.getBytes());
            return bytesToHex(hash).substring(0, 40); // simplificado
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySignature(String signature, String pubKey) {
    return signature.equals("Valid Signature" + pubKey);
}

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}