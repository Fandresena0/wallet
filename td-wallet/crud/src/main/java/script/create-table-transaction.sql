CREATE TABLE IF NOT EXISTS transaction (
    id_transaction serial PRIMARY KEY,
    label varchar(50),
    montant numeric(10,2),
    date_heure timestamp,
    type_transaction varchar(7) CHECK (type_transaction IN ('Débit', 'Crédit'))
    );

INSERT INTO transaction (label, montant, date_heure, type_transaction)
VALUES
    ('Prêt bancaire', 5000.00, '2023-12-08 10:30:00', 'Crédit'),
    ('Achat', 150.50, '2023-12-08 12:45:00', 'Débit'),
    ('Salaire', 3000.00, '2023-12-08 15:00:00', 'Crédit');
