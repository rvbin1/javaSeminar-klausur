package enums;

public class AppConstants {

    private AppConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    public static final String ACCOUNT_FILE = "accounts.json";
    public static final String ALGORITHM = "SHA-256";
    public static final int CHAT_REFRESH_INTERVAL_MS = 2000;

}