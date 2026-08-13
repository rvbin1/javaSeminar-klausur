package controllers;

import models.AccountModel;
import utils.services.LoginAccountService;
import utils.SessionServiceSingelton;
import views.MainFrameView;

public class LoginController {
    private MainFrameView mainFrameView;

    public LoginController(MainFrameView mainFrameView, ChatPanelController chatPanelController) {
        this.mainFrameView = mainFrameView;

        this.mainFrameView.getLoginPanelView().getRegisterButton()
                .addActionListener(e -> {
                    this.mainFrameView.showRegisterPanelView();
                });

        this.mainFrameView.getLoginPanelView().getLoginButton()
                .addActionListener(e -> {
                    char[] passwordChars = this.mainFrameView.getLoginPanelView().getPasswordField().getPassword();

                    AccountModel account = LoginAccountService.checkLogin(
                            this.mainFrameView.getLoginPanelView().getUsernameField().getText(),
                            new String(passwordChars));

                    if (account != null) {
                        SessionServiceSingelton.getInstance().setCurrentAccount(account);
                        chatPanelController.loadContacts();
                        this.mainFrameView.showMainChatView();
                    }
                });
    }
}