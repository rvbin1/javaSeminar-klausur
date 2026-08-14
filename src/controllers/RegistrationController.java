package controllers;

import utils.services.RegisterAccountService;
import views.MainFrameView;
import views.login.RegistrationPanelView;

public class RegistrationController {
    private final MainFrameView mainFrameView;

    public RegistrationController(MainFrameView mainFrameView) {
        this.mainFrameView = mainFrameView;

        getMainFrameView().getRegistrationPanelView().getRegisterButton()
                .addActionListener(e -> {
                    RegistrationPanelView rgv = getMainFrameView().getRegistrationPanelView();

                    String userName = rgv.getUsernameField().getText();
                    String password = new String(rgv.getPasswordField().getPassword());

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

                    RegisterAccountService.createAccount(userName, password);
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