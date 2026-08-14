package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatModel {
    private UUID chatID;
    private AccountModel chatter1;
    private AccountModel chatter2;
    private ArrayList<ChatMessageModel> messages;

    public ChatModel(AccountModel chatter1, AccountModel chatter2) {
        this.setChatID(UUID.randomUUID());
        this.setChatter1(chatter1);
        this.setChatter2(chatter2);
        this.messages = new ArrayList<>();
    }

    public UUID getChatID() {
        return chatID;
    }

    private void setChatID(UUID chatID) {
        this.chatID = chatID;
    }

    public AccountModel getChatter1() {
        return chatter1;
    }

    public void setChatter1(AccountModel chatter1) {
        this.chatter1 = chatter1;
    }

    public AccountModel getChatter2() {
        return chatter2;
    }

    public void setChatter2(AccountModel chatter2) {
        this.chatter2 = chatter2;
    }

    public List<ChatMessageModel> getMessages() {
        // Defensive Kopie: der Aufrufer darf die zurueckgegebene Liste veraendern,
        // ohne dass sich das auf den internen Zustand von ChatModel auswirkt.
        return new ArrayList<>(messages);
    }

    public void addMessage(ChatMessageModel message) {
        messages.add(message);
    }
}
