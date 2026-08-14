package controllers;

import enums.LoginStatus;
import models.AccountModel;
import utils.services.LoginAccountService;
import utils.services.LoginAccountService.LoginResult;
import utils.SessionHandler;
import views.MainFrameView;

public class LoginController {
    private final MainFrameView mainFrameView;

    public LoginController(MainFrameView mainFrameView, ChatPanelController chatPanelController) {
        this.mainFrameView = mainFrameView;

        getMainFrameView().getLoginPanelView().getRegisterButton()
                .addActionListener(e -> {
                    getMainFrameView().showRegisterPanelView();
                });

        getMainFrameView().getLoginPanelView().getLoginButton()
                .addActionListener(e -> {
                    char[] passwordChars = getMainFrameView().getLoginPanelView().getPasswordField().getPassword();

                    String userName = getMainFrameView().getLoginPanelView().getUsernameField().getText();
                    String password = new String(passwordChars);

                    LoginResult result = LoginAccountService.checkLogin(userName, password);

                    if (result.getStatus() == LoginStatus.USER_NOT_FOUND) {
                        javax.swing.JOptionPane.showMessageDialog(
                                getMainFrameView(),
                                "Der Benutzername \"" + userName + "\" existiert nicht.",
                                "Fehler",
                                javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    if (result.getStatus() == LoginStatus.WRONG_PASSWORD) {
                        javax.swing.JOptionPane.showMessageDialog(
                                getMainFrameView(),
                                "Das Passwort ist falsch.",
                                "Fehler",
                                javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    AccountModel account = result.getAccount();
                    SessionHandler.getInstance().setCurrentAccount(account);
                    chatPanelController.loadContacts();
                    getMainFrameView().showMainChatView();
                });
    }

    public MainFrameView getMainFrameView() {
        return mainFrameView;
    }


}