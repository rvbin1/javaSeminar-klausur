package utils.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

        Type listType = new TypeToken<ArrayList<AccountModel>>(){}.getType();
        ArrayList<AccountModel> accounts = GSON.fromJson(json, listType);

        return accounts != null ? accounts : new ArrayList<>();
    }
}
