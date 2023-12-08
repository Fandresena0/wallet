CREATE TYPE IF NOT EXISTS devise_nom AS ENUM ('Euro', 'Ariary');

CREATE TABLE IF NOT EXISTS devise (
        id_devise serial PRIMARY KEY,
        nom_devise devise_nom,
        code_devise varchar(3) CHECK (code_devise IN ('EUR', 'MGA'))
    );

INSERT INTO devise (nom_devise, code_devise)
VALUES
    ('Euro', 'EUR'),
    ('Ariary', 'MGA');
