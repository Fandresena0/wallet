CREATE TABLE IF NOT EXISTS compte (
   id_compte serial  PRIMARY KEY,
   nom_utilisateur varchar(50),
   id_transaction INT REFERENCES transaction(id_transaction) NOT NULL
   id_devise INT REFERENCES devise(id_devise) NOT NULL
);

INSERT INTO compte VALUES ('Paris',3,1),('ROMAN',2,2),('Norman',4,3);