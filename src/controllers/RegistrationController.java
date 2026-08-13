package controllers;

import utils.services.RegisterAccountService;
import views.MainFrameView;
import views.login.RegistrationPanelView;

public class RegistrationController {
    private MainFrameView mainFrameView;
    private RegisterAccountService ras = new RegisterAccountService();

    public RegistrationController(MainFrameView mainFrameView) {
        this.mainFrameView = mainFrameView;

        this.mainFrameView.getRegistrationPanelView().getRegisterButton()
                .addActionListener(e -> {
                    RegistrationPanelView rgv = this.mainFrameView.getRegistrationPanelView();

                    ras.createAccount(rgv.getUsernameField().getText(),
                            new String(rgv.getPasswordField().getPassword()));
                });

        this.mainFrameView.getRegistrationPanelView().getCancelButton()
                .addActionListener(e -> {
                    this.mainFrameView.showLoginPanelView();
                });
    }
}
