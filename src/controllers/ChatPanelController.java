package controllers;

import models.AccountModel;
import models.ChatMessageModel;
import models.ChatModel;
import utils.services.ChatService;
import utils.SessionHandler;
import utils.services.UserService;
import views.MainFrameView;
import views.chat.ChatPanelView;
import views.chat.SidePanelView;

import java.util.List;

public class ChatPanelController {
    private final MainFrameView mainFrameView;
    private final SidePanelView sidePanel;
    private final ChatPanelView chatPanel;
    private String currentPartner;

    public ChatPanelController(MainFrameView mainFrameView) {
        this.mainFrameView = mainFrameView;
        this.sidePanel = mainFrameView.getMainChatView().getSidePanel();
        this.chatPanel = mainFrameView.getMainChatView().getChatPanel();

        initAddChatButton();
        initContactSelection();
        initSendMessage();
    }

    private void initAddChatButton() {
        sidePanel.getAddChatButton().addActionListener(e -> {
            if (sidePanel.getSearchField().getText().isEmpty()) {
                return;
            }

            String chatter1 = SessionHandler.getInstance().getCurrentAccount().getUsername();
            String chatter2 = sidePanel.getSearchField().getText();

            ChatService.createChat(UserService.getAccountByUserName(chatter1),
                    UserService.getAccountByUserName(chatter2));

            ChatModel chat = ChatService.findChat(UserService.getAccountByUserName(chatter1),
                    UserService.getAccountByUserName(chatter2));

            if (chat != null) {
                sidePanel.getContactListModel().addElement(chatter2);

                int count = sidePanel.getContactListModel().getSize();
                sidePanel.getStatusLabel().setText(count + " Chats");

                sidePanel.getSearchField().setText("");
            } else {
                javax.swing.JOptionPane.showMessageDialog(
                        mainFrameView,
                        "Chat konnte nicht erstellt werden. Existiert der Benutzer \"" + chatter2 + "\"?",
                        "Fehler",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void initContactSelection() {
        sidePanel.getChatList().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            String selected = sidePanel.getChatList().getSelectedValue();
            if (selected == null) return;

            openChat(selected);
        });
    }

    private void openChat(String partnerUsername) {
        currentPartner = partnerUsername;

        chatPanel.getContactLabel().setText(partnerUsername);
        chatPanel.getMessageField().setEnabled(true);
        chatPanel.getSendButton().setEnabled(true);

        loadMessages(partnerUsername);
    }

    private void loadMessages(String partnerUsername) {
        String me = SessionHandler.getInstance().getCurrentAccount().getUsername();
        ChatModel chat = ChatService.findChat(
                UserService.getAccountByUserName(me),
                UserService.getAccountByUserName(partnerUsername));

        chatPanel.getChatArea().setText("");
        if (chat == null) return;

        List<ChatMessageModel> messages = chat.getMessages();
        for (ChatMessageModel m : messages) {
            chatPanel.getChatArea().append(m.getSender().getUsername() + ": " + m.getContent() + "\n");
        }
    }

    private void initSendMessage() {
        Runnable send = () -> {
            // Nachrichten werden direkt in den aktuellen Chat geschrieben und anschließend neu geladen.
            if (currentPartner == null) return;
            String text = chatPanel.getMessageField().getText().trim();
            if (text.isEmpty()) return;

            String me = SessionHandler.getInstance().getCurrentAccount().getUsername();
            AccountModel sender = UserService.getAccountByUserName(me);
            AccountModel receiver = UserService.getAccountByUserName(currentPartner);

            ChatService.sendMessage(sender, receiver, text);

            chatPanel.getMessageField().setText("");
            loadMessages(currentPartner);
        };

        chatPanel.getSendButton().addActionListener(e -> send.run());
        chatPanel.getMessageField().addActionListener(e -> send.run());
    }

    public void loadContacts() {
        // Die Kontaktliste wird aus den vorhandenen Chats des eingeloggten Nutzers aufgebaut.
        sidePanel.getContactListModel().clear();

        String me = SessionHandler.getInstance().getCurrentAccount().getUsername();
        AccountModel myAccount = UserService.getAccountByUserName(me);

        List<ChatModel> chats = ChatService.getChatsForUser(myAccount);

        for (ChatModel chat : chats) {
            String partner = chat.getChatter1().getUsername().equalsIgnoreCase(me)
                    ? chat.getChatter2().getUsername()
                    : chat.getChatter1().getUsername();

            sidePanel.getContactListModel().addElement(partner);
        }

        int count = sidePanel.getContactListModel().getSize();
        sidePanel.getStatusLabel().setText(count + " Chats");
    }
}