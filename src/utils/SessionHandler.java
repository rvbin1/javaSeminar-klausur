package utils;

import models.AccountModel;

public class SessionHandler {
    private static SessionHandler instance;
    private AccountModel currentAccount;

    private SessionHandler() {}

    public static SessionHandler getInstance() {
        if (instance == null) {
            instance = new SessionHandler();
        }
        return instance;
    }

    public void setCurrentAccount(AccountModel currentAccount) {
        this.currentAccount = currentAccount;
    }

    public AccountModel getCurrentAccount() {
        return currentAccount;
    }
}

