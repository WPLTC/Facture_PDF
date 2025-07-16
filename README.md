# Application de gestion de factures pour micro-entrepreneur

## Technologies utilisées
- Java 11+
- JavaFX (interface graphique)
- PostgreSQL (base de données, gestion via PgAdmin)
- JDBC PostgreSQL
- VS Code (IDE)

## Structure du projet

```
Facture/
│
├── lib/                  # Librairies JavaFX et JDBC
├── src/                  # Code source Java
│   ├── Main.java         # Point d'entrée de l'application
│   ├── LoginController.java  # Contrôleur de la fenêtre de connexion
│   └── User.java         # Modèle utilisateur
├── db.properties         # Configuration de la base de données
└── README.md             # Ce fichier
```

## Prérequis
- Java JDK 11 ou supérieur
- JavaFX SDK
- PostgreSQL installé et accessible
- VS Code avec Java Extension Pack

## Installation
1. Télécharger JavaFX SDK et le JDBC PostgreSQL, placer les .jar dans `lib/`
2. Configurer la base PostgreSQL (voir script SQL plus bas)
3. Adapter `db.properties` avec vos identifiants
4. Compiler et lancer le projet avec les bons chemins de librairies

## Script SQL pour la table utilisateur
```sql
CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL
);
INSERT INTO utilisateur (login, mot_de_passe) VALUES ('admin', 'admin');
```

## Lancement (exemple)
```sh
javac --module-path lib/javafx-sdk-XX/lib --add-modules javafx.controls,javafx.fxml -cp "lib/*;src" src/Main.java
java --module-path lib/javafx-sdk-XX/lib --add-modules javafx.controls,javafx.fxml -cp "lib/*;src" Main
```

Adaptez les chemins selon votre installation. 