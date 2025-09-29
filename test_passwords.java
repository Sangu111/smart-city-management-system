import java.security.MessageDigest;

public class test_passwords {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "ERROR";
        }
    }
    
    public static void main(String[] args) {
        System.out.println("admin123 hash: " + hashPassword("admin123"));
        System.out.println("test hash: " + hashPassword("test"));
    }
}