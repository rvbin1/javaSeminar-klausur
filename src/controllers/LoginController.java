package controllers;

import enums.LoginStatus;
import models.AccountModel;
import utils.services.LoginAccountService;
import utils.services.LoginAccountService.LoginResult;
import utils.SessionHandler;
import views.MainFrameView;

import javax.swing.JOptionPane;

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
                    // JPasswordField liefert bewusst ein char[] statt String (Strings sind
                    // unveraenderlich und bleiben laenger im Speicher) - wird hier direkt weiterverwendet.
                    char[] passwordChars = getMainFrameView().getLoginPanelView().getPasswordField().getPassword();

                    String userName = getMainFrameView().getLoginPanelView().getUsernameField().getText();
                    String password = new String(passwordChars);

                    LoginResult result = LoginAccountService.checkLogin(userName, password);

                    if (result.getStatus() == LoginStatus.USER_NOT_FOUND) {
                        showError("Der Benutzername \"" + userName + "\" existiert nicht.");
                        return;
                    }

                    if (result.getStatus() == LoginStatus.WRONG_PASSWORD) {
                        showError("Das Passwort ist falsch.");
                        return;
                    }

                    AccountModel account = result.getAccount();
                    SessionHandler.getInstance().setCurrentAccount(account);
                    chatPanelController.loadContacts();
                    getMainFrameView().showMainChatView();
                });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(mainFrameView, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    public MainFrameView getMainFrameView() {
        return mainFrameView;
    }


}
