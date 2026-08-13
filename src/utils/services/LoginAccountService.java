package utils.services;

import models.AccountModel;

public class LoginAccountService {

    public static AccountModel checkLogin(String userName, String password)
    {
        if (UserService.getAccountByUserName(userName) == null) return null;

        if (!UserService.getAccountByUserName(userName).verifyPassword(password)) return null;

        return UserService.getAccountByUserName(userName);
    }
}
