CREATE TABLE IF NOT EXISTS compte (
    id_compte serial PRIMARY KEY,
    nom_compte varchar(50),
    solde double,
    id_transaction INT REFERENCES transaction(id_transaction) NOT NULL,
    id_devise INT REFERENCES devise(id_devise) NOT NULL,
    type_compte varchar(20) CHECK (type_compte IN ('Banque', 'Espèce', 'Mobile Money'))
    );

INSERT INTO compte (nom_compte, solde_montant, solde_date_maj, id_transaction, id_devise, type_compte)
VALUES
    ('Compte Courant', 1000.00, '2023-12-08', 1, 1, 'Banque'),
    ('Compte Épargne', 5000.00, '2023-12-08', 2, 2, 'Banque'),
    ('Portefeuille', 200.00, '2023-12-08', 3, 3, 'Mobile Money');
