-- Création de la table utilisateur
CREATE TABLE IF NOT EXISTS utilisateur (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL
);

-- Insertion d'un utilisateur par défaut
INSERT INTO utilisateur (login, mot_de_passe) VALUES ('admin', 'admin'); 