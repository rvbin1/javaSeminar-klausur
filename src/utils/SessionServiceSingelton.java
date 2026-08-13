package utils;

import models.AccountModel;

public class SessionServiceSingelton {
    private static SessionServiceSingelton instance;
    private AccountModel currentAccount;

    private SessionServiceSingelton() {}

    public static SessionServiceSingelton getInstance() {
        if (instance == null) {
            instance = new SessionServiceSingelton();
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

