package views.chat;

import javax.swing.*;
import java.awt.*;

public class MainChatView extends JPanel {

    private final SidePanelView sidePanel = new SidePanelView();
    private final ChatPanelView chatPanel = new ChatPanelView();

    public MainChatView() {
        setLayout(new BorderLayout());
        add(sidePanel, BorderLayout.WEST);
        add(chatPanel, BorderLayout.CENTER);
    }

    public SidePanelView getSidePanel() {
        return sidePanel;
    }

    public ChatPanelView getChatPanel() {
        return chatPanel;
    }

    public JTextArea getChatArea() {
        return chatPanel.getChatArea();
    }

    public JTextField getMessageField() {
        return chatPanel.getMessageField();
    }

    public JButton getSendButton() {
        return chatPanel.getSendButton();
    }

    public JLabel getContactLabel() {
        return chatPanel.getContactLabel();
    }

    public JList<String> getChatList() {
        return sidePanel.getChatList();
    }

    public JButton getAddChatButton() {
        return sidePanel.getAddChatButton();
    }

    public JLabel getStatusLabel() {
        return sidePanel.getStatusLabel();
    }
}