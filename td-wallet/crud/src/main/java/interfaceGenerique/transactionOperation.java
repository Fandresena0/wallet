package interfaceGenerique;

import model.Compte;
import model.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface transactionOperation {
    Transaction create(Transaction toInsert);
    List<Transaction> readAll() throws SQLException;
    Transaction delete(Transaction toDelete) throws SQLException;
    Transaction update(Transaction existingTransaction, Transaction newTransactionDetails);
    Compte effectuerTransaction(Compte compte, Transaction transaction);
    void enregistrerTransactionDansDB(Transaction transaction, Compte compte) throws SQLException;

}
