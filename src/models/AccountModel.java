package models;

import utils.services.PasswordHashService;

import java.util.UUID;

public class AccountModel {
    private UUID id;
    private String username;
    private String password;

    public AccountModel(String username, String password) {
        this.setId(UUID.randomUUID());
        this.setUsername(username);
        this.setPassword(password);
    }

    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // Das Passwort wird beim Speichern sofort gehasht, damit es nicht im Klartext
        // in der JSON-Datei landet.
        this.password = PasswordHashService.hashPassword(password);
    }

    public boolean verifyPassword(String rawPassword) {
        return PasswordHashService.verifyPassword(rawPassword, this.getPassword());
    }
}
