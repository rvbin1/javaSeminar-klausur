package controllers;

import models.AccountModel;
import utils.SessionHandler;
import utils.services.RegisterAccountService;
import views.MainFrameView;
import views.login.RegistrationPanelView;

public class RegistrationController {
    private final MainFrameView mainFrameView;

    public RegistrationController(MainFrameView mainFrameView, ChatPanelController chatPanelController) {
        this.mainFrameView = mainFrameView;

        getMainFrameView().getRegistrationPanelView().getRegisterButton()
                .addActionListener(e -> {
                    RegistrationPanelView rgv = getMainFrameView().getRegistrationPanelView();

                    String userName = rgv.getUsernameField().getText();
                    String password = new String(rgv.getPasswordField().getPassword());

                    if (RegisterAccountService.isUsernameEmpty(userName)) {
                        javax.swing.JOptionPane.showMessageDialog(
                                getMainFrameView(),
                                "Bitte gib einen Benutzernamen ein.",
                                "Fehler",
                                javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    if (RegisterAccountService.isUsernameTaken(userName)) {
                        javax.swing.JOptionPane.showMessageDialog(
                                getMainFrameView(),
                                "Der Benutzername \"" + userName + "\" ist bereits vergeben.",
                                "Fehler",
                                javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    if (RegisterAccountService.isPasswordTooShort(password)) {
                        javax.swing.JOptionPane.showMessageDialog(
                                getMainFrameView(),
                                "Das Passwort muss mindestens 5 Zeichen lang sein.",
                                "Fehler",
                                javax.swing.JOptionPane.ERROR_MESSAGE
                        );
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

    public MainFrameView getMainFrameView() {
        return this.mainFrameView;
    }
}