package utils.services;

public class PasswordHashService {

    public static String hashPassword(String password)
    {
        return HashService.sha256(password);
    }

    public static boolean verifyPassword(String password, String hashedPassword)
    {
        // Hashing ist eine Einwegfunktion: der gespeicherte Hash wird nie zurueck in ein
        // Passwort verwandelt, stattdessen wird die Eingabe erneut gehasht und verglichen.
        return hashPassword(password).equals(hashedPassword);
    }
}
