package utils.services;

public class PasswordHashService {

    public static String hashPassword(String password)
    {
        return HashService.sha256(password);
    }

    public static boolean verifyPassword(String password, String hashedPassword)
    {
        return hashPassword(password).equals(hashedPassword);
    }
}
