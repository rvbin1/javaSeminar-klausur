package controllers;

import models.AccountModel;
import utils.SessionHandler;
import utils.services.RegisterAccountService;
import views.MainFrameView;
import views.login.RegistrationPanelView;

import javax.swing.JOptionPane;

public class RegistrationController {
    private final MainFrameView mainFrameView;

    public RegistrationController(MainFrameView mainFrameView, ChatPanelController chatPanelController) {
        this.mainFrameView = mainFrameView;

        getMainFrameView().getRegistrationPanelView().getRegisterButton()
                .addActionListener(e -> {
                    RegistrationPanelView rgv = getMainFrameView().getRegistrationPanelView();

                    String userName = rgv.getUsernameField().getText();
                    // JPasswordField liefert bewusst ein char[] statt String (Strings sind
                    // unveraenderlich und bleiben laenger im Speicher) - wird hier direkt weiterverwendet.
                    String password = new String(rgv.getPasswordField().getPassword());

                    if (RegisterAccountService.isUsernameEmpty(userName)) {
                        showError("Bitte gib einen Benutzernamen ein.");
                        return;
                    }

                    if (RegisterAccountService.isUsernameTaken(userName)) {
                        showError("Der Benutzername \"" + userName + "\" ist bereits vergeben.");
                        return;
                    }

                    if (RegisterAccountService.isPasswordTooShort(password)) {
                        showError("Das Passwort muss mindestens 5 Zeichen lang sein.");
                        return;
                    }

                    AccountModel account = RegisterAccountService.createAccount(userName, password);

                    SessionHandler.getInstance().setCurrentAccount(account);
                    chatPanelController.loadContacts();
                    getMainFrameView().showMainChatView();
                });

        getMainFrameView().getRegistrationPanelView().getCancelButton()
                .addActionListener(e -> {
                    getMainFrameView().showLoginPanelView();
                });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(mainFrameView, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    public MainFrameView getMainFrameView() {
        return this.mainFrameView;
    }
}
