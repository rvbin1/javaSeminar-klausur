package utils;

import models.AccountModel;

// Singleton, da pro laufender App-Instanz immer nur ein Benutzer eingeloggt ist
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

