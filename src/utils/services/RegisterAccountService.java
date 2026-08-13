package utils.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.AppConstants;
import models.AccountModel;

import java.util.ArrayList;

public class RegisterAccountService {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void createAccount(String userName, String password) {
        AccountModel account = new AccountModel(userName, password);

        ArrayList<AccountModel> accounts = ParseAccountService.parseAllAccounts();
        accounts.add(account);

        String json = GSON.toJson(accounts);
        FileHandlerService.writeFile(AppConstants.ACCOUNT_FILE, json);
    }


}
