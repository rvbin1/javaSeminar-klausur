package utils.services;

import models.AccountModel;

import java.util.ArrayList;

public class UserService {
    public static AccountModel getAccountByUserName(String userName)
    {
        ArrayList<AccountModel> accounts = ParseAccountService.parseAllAccounts();

        for (AccountModel account : accounts) {
            if (account.getUsername().equals(userName)) {
                return account;
            }
        }
        return null;
    }
}
