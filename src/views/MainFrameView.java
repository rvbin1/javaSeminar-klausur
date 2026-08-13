package views;

import enums.MainFrameViews;
import views.chat.MainChatView;
import views.login.LoginPanelView;
import views.login.RegistrationPanelView;

import javax.swing.*;
import java.awt.*;

public class MainFrameView extends JFrame {

    private final JPanel mainPanel;
    private final LoginPanelView loginPanelView = new LoginPanelView();
    private final RegistrationPanelView registrationPanelView = new RegistrationPanelView();
    private final MainChatView mainChatView = new MainChatView();

    public MainFrameView() {
        super.setTitle("Chat Messenger");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(1000, 600);
        super.setLocationRelativeTo(null);

        mainPanel = new JPanel(new CardLayout());

        this.getMainPanel().add(this.getLoginPanelView(), MainFrameViews.LOGIN.name());
        this.getMainPanel().add(this.getRegistrationPanelView(), MainFrameViews.REGISTRATION.name());
        this.getMainPanel().add(this.getMainChatView(), MainFrameViews.CHAT.name());


        super.add(getMainPanel());

        this.showLoginPanelView();

        super.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public LoginPanelView getLoginPanelView() {
        return loginPanelView;
    }

    public RegistrationPanelView getRegistrationPanelView() {
        return registrationPanelView;
    }

    public MainChatView getMainChatView() {
        return mainChatView;
    }

    public void showLoginPanelView() {
        CardLayout cardLayout = (CardLayout) getMainPanel().getLayout();
        cardLayout.show(getMainPanel(), MainFrameViews.LOGIN.name());
    }

    public void showRegisterPanelView() {
        CardLayout cardLayout = (CardLayout) getMainPanel().getLayout();
        cardLayout.show(getMainPanel(), MainFrameViews.REGISTRATION.name());
    }

    public void showMainChatView() {
        CardLayout cardLayout = (CardLayout) getMainPanel().getLayout();
        cardLayout.show(getMainPanel(), MainFrameViews.CHAT.name());
    }
}
