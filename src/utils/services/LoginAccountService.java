package utils.services;

import enums.LoginStatus;
import models.AccountModel;

public class LoginAccountService {

    public static class LoginResult {
        private final LoginStatus status;
        private final AccountModel account;

        public LoginResult(LoginStatus status, AccountModel account) {
            this.status = status;
            this.account = account;
        }

        public LoginStatus getStatus() {
            return status;
        }

        public AccountModel getAccount() {
            return account;
        }
    }

    // Status und Account werden bewusst getrennt zurueckgegeben, damit der Controller
    // "Benutzer existiert nicht" von "Passwort falsch" unterscheiden und passend anzeigen kann.
    public static LoginResult checkLogin(String userName, String password) {
        AccountModel account = UserService.getAccountByUserName(userName);

        if (account == null) {
            return new LoginResult(LoginStatus.USER_NOT_FOUND, null);
        }

        if (!account.verifyPassword(password)) {
            return new LoginResult(LoginStatus.WRONG_PASSWORD, null);
        }

        return new LoginResult(LoginStatus.SUCCESS, account);
    }
}