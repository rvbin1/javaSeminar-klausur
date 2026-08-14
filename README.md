# Java Klausur – Chat-Anwendung

Eine kleine Java-Swing-Anwendung für lokale Benutzerregistrierung, Login und Direktnachrichten. Die Daten werden als JSON-Dateien im Projektordner gespeichert, damit die App ohne Datenbank direkt genutzt werden kann.

## Funktionen

- Registrierung neuer Benutzer
- Login mit Benutzername und Passwort
- Chat-Erstellung zwischen zwei Nutzern
- Nachrichtenfluss im Hauptfenster
- Persistenz über JSON-Dateien (`accounts.json`, `*-chat.json`)

## Projektstruktur

- `src/Main.java` – Einstiegspunkt der Swing-App
- `src/controllers/` – Login-, Registrierungs- und Chat-Controller
- `src/views/` – GUI-Komponenten
- `src/models/` – Datenmodelle wie `AccountModel` und `ChatModel`
- `src/utils/` – Services für Authentifizierung, Datei-Handling und Chats
- `src/enums/` – zentrale Konstanten

## Wichtige Mechanismen

- `AccountModel` speichert Benutzer und hasht Passwörter beim Setzen.
- `LoginAccountService` prüft den Login gegen die gespeicherten Accounts.
- `ChatService` erzeugt und lädt Chats über einen deterministischen Schlüssel, damit gleiche Paare immer dieselbe Datei teilen.
- `FileHandlerService` liest und schreibt die JSON-Daten auf der Festplatte.
- `SessionService` hält den aktuell angemeldeten Benutzer im Speicher.

## Voraussetzung

Java JDK 17 oder höher und GSON 2.11.0
Beim ersten Start werden ggf. die benötigten JSON-Dateien automatisch erzeugt bzw. beim Login/Chatten ergänzt.

## Hinweis

Die App ist bewusst leichtgewichtig und ohne externe Bibliotheken aufgebaut. Für Produktionsfälle wäre eine echte Datenbank sowie zusätzliche Sicherheitsmaßnahmen sinnvoll.
