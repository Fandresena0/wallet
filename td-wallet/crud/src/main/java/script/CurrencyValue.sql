CREATE TABLE CurrencyValue (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    ID_Devise_source INT,
    ID_Devise_destination INT,
    Montant DOUBLE,
    Date_effet DATE,
    FOREIGN KEY (ID_Devise_source) REFERENCES Devise (ID_Devise),
    FOREIGN KEY (ID_Devise_destination) REFERENCES Devise (ID_Devise)
);
