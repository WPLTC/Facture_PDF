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
    heure_debut TIME,          -- pour formation
    heure_fin TIME,            -- pour formation
    classe VARCHAR(50),        -- pour formation
    module VARCHAR(100),       -- pour formation
    description TEXT,          -- pour consultation
    tjm NUMERIC(10,2),         -- pour consultation
    client_id INTEGER REFERENCES client(id),
    entreprise VARCHAR(100)
);

-- Insertion d'un client de test
INSERT INTO client (nom, entreprise) VALUES ('ClientTest', 'EntrepriseTest'); 
ALTER TABLE prestation ADD COLUMN IF NOT EXISTS tarif_horaire NUMERIC(10,2);
ALTER TABLE prestation ADD COLUMN IF NOT EXISTS nb_jours INTEGER; 