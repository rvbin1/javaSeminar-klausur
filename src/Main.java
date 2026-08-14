import controllers.ChatPanelController;
import controllers.LoginController;
import controllers.RegistrationController;
import views.MainFrameView;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Fenster und Controller werden hier verbunden: Login, Registrierung und Chat-Logik.
            MainFrameView mainFrameView = new MainFrameView();
            ChatPanelController chatPanelController = new ChatPanelController(mainFrameView);
            LoginController loginController = new LoginController(mainFrameView, chatPanelController);
            RegistrationController registrationController = new RegistrationController(mainFrameView, chatPanelController);
        });
    }
}