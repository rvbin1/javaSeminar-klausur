package utils.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import enums.AppConstants;
import models.AccountModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ParseAccountService {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ArrayList<AccountModel> parseAllAccounts() {
        String json = FileHandlerService.readFile(AppConstants.ACCOUNT_FILE);

        if (json == null || json.isBlank()) {
            return new ArrayList<>();
        }

        // Gson kennt zur Laufzeit nicht "ArrayList<AccountModel>.class".
        // TypeToken haelt diese generische Typinformation ueber eine anonyme Unterklasse fest,
        // damit Gson die JSON-Liste in die richtigen AccountModel-Objekte umwandeln kann.
        Type listType = new TypeToken<ArrayList<AccountModel>>(){}.getType();

        // Ist accounts.json beschaedigt oder von Hand fehlerhaft bearbeitet worden,
        // soll die App mit einer leeren Benutzerliste weiterlaufen statt abzustuerzen.
        ArrayList<AccountModel> accounts;
        try {
            accounts = GSON.fromJson(json, listType);
        } catch (JsonSyntaxException e) {
            System.err.println("Fehler beim Lesen von " + AppConstants.ACCOUNT_FILE + ": " + e.getMessage());
            return new ArrayList<>();
        }

        return accounts != null ? accounts : new ArrayList<>();
    }
}
