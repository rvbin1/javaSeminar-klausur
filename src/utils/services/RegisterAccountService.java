package utils.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.AppConstants;
import models.AccountModel;

import java.util.ArrayList;

public class RegisterAccountService {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final int MIN_PASSWORD_LENGTH = 5;

    public static boolean isUsernameTaken(String userName) {
        ArrayList<AccountModel> accounts = ParseAccountService.parseAllAccounts();
        return accounts.stream()
                .anyMatch(account -> account.getUsername().equalsIgnoreCase(userName));
    }

    public static boolean isPasswordTooShort(String password) {
        return password == null || password.length() < MIN_PASSWORD_LENGTH;
    }

    public static AccountModel createAccount(String userName, String password) {
        AccountModel account = new AccountModel(userName, password);

        ArrayList<AccountModel> accounts = ParseAccountService.parseAllAccounts();
        accounts.add(account);

        String json = GSON.toJson(accounts);
        FileHandlerService.writeFile(AppConstants.ACCOUNT_FILE, json);

        return account;
    }

    public static boolean isUsernameEmpty(String userName) {
        return userName == null || userName.trim().isEmpty();
    }
}