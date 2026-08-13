package models;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChatMessageModel {
        private UUID messageId;
        private AccountModel sender;
        private AccountModel receiver;
        private String content;
        private LocalDateTime timestamp;

        public ChatMessageModel(AccountModel sender, AccountModel receiver, String content) {
            this.setMessageId(UUID.randomUUID());
            this.setSender(sender);
            this.setReceiver(receiver);
            this.setContent(content);
            this.setTimestamp(LocalDateTime.now());
        }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public AccountModel getSender() {
        return sender;
    }

    public void setSender(AccountModel sender) {
        this.sender = sender;
    }

    public AccountModel getReceiver() {
        return receiver;
    }

    public void setReceiver(AccountModel receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
