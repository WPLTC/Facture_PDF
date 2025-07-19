# Application de gestion de factures pour micro-entrepreneur

## Technologies utilisées
- Java 11+
- JavaFX (interface graphique)
- PostgreSQL (base de données)
- JDBC PostgreSQL
- VS Code (ou tout IDE Java)

---

## Structure du projet

```
Facture_PDF/
│
├── lib/                  # Librairies JavaFX, JDBC, PDFBox, etc.
├── src/                  # Code source Java et FXML
├── sql/                  # Scripts SQL d'initialisation de la base
├── db.properties         # Configuration de la base de données
└── README.md             # Ce fichier
```

---

## Prérequis

- Java JDK 11 ou supérieur
- JavaFX SDK (ex : javafx-sdk-17.x)
- PostgreSQL installé et accessible
- Les .jar nécessaires dans le dossier `lib/` (JavaFX, JDBC, PDFBox…)

---

## Installation & Initialisation

1. **Installer les dépendances**
   - Télécharge JavaFX SDK et JDBC PostgreSQL, place les .jar dans `lib/`
2. **Configurer la base PostgreSQL**
   - Crée une base nommée `Facturation` (ou adapte le nom dans `db.properties`)
   - Exécute les deux scripts SQL d'initialisation :
     ```sh
     psql -U postgres -d Facturation -f sql/init.sql
     psql -U postgres -d Facturation -f sql/init_prestation_client.sql
     
     ```
3. **Configurer la connexion**
   - Modifie `db.properties` si besoin :
     ```
     url=jdbc:postgresql://localhost:5432/Facturation
     user=postgres
     password=admin
     ```
4. **Compiler le projet**
   ```sh
   javac --module-path lib/javafx-sdk-17.0.16/lib --add-modules javafx.controls,javafx.fxml -cp "lib/*;src" src\\*.java
   ```
5. **Lancer l'application**
   ```sh
   java --module-path lib/javafx-sdk-17.0.16/lib --add-modules javafx.controls,javafx.fxml -cp "lib/*;." src.Main
   ```

---

## Exemple de script SQL d'initialisation

```sql
-- Création de la table client
CREATE TABLE IF NOT EXISTS client (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    entreprise VARCHAR(100)
);

-- Création de la table prestation
CREATE TABLE IF NOT EXISTS prestation (
    id SERIAL PRIMARY KEY,
    type VARCHAR(20) NOT NULL, -- 'formation' ou 'consultation'
    date_prestation DATE NOT NULL,
    heure_debut TIME,
    heure_fin TIME,
    classe VARCHAR(50),
    module VARCHAR(100),
    description TEXT,
    tjm NUMERIC(10,2),
    client_id INTEGER REFERENCES client(id),
    entreprise VARCHAR(100)
);

-- Insertion d'un client de test
INSERT INTO client (nom, entreprise) VALUES ('ClientTest', 'EntrepriseTest');
ALTER TABLE prestation ADD COLUMN IF NOT EXISTS tarif_horaire NUMERIC(10,2);
ALTER TABLE prestation ADD COLUMN IF NOT EXISTS nb_jours INTEGER;
```

---

## Connexion par défaut

- **Login** : `admin`
- **Mot de passe** : `admin`

---

## Conseils

- Pour lancer l’application, adapte le chemin JavaFX si besoin.
- Si vous voulez exécuter d’autres scripts SQL, place-les dans le dossier `sql/` et adapte la commande `psql`.
- Pour toute erreur de connexion, vérifie bien le contenu de `db.properties` et que PostgreSQL est démarré. 