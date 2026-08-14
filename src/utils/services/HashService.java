package utils.services;

import enums.AppConstants;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashService {

    public static String sha256(String input) {
        try {
            byte[] hash = MessageDigest.getInstance(AppConstants.ALGORITHM).digest(input.getBytes());
            // URL-sicheres Base64 statt Standard-Base64, weil das Ergebnis (z. B. in ChatService)
            // direkt als Dateiname verwendet wird und dort keine "/" auftauchen duerfen.
            return Base64.getUrlEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
