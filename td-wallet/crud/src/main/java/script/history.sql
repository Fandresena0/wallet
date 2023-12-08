
CREATE TABLE TransferHistory (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_transaction_debiteur INT,
    id_transaction_crediteur INT,
    dateTransfert TIMESTAMP,
    FOREIGN KEY (id_transaction_debiteur) REFERENCES transaction (id_transaction),
    FOREIGN KEY (id_transaction_crediteur) REFERENCES transaction (id_transaction)
);
