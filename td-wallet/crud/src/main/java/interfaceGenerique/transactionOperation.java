package interfaceGenerique;

import model.Compte;
import model.Transaction;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface transactionOperation {
    Transaction create(Transaction toInsert);
    List<Transaction> readAll() throws SQLException;
    Transaction delete(Transaction toDelete) throws SQLException;
    Transaction update(Transaction existingTransaction, Transaction newTransactionDetails);
    Compte effectuerTransaction(Compte compte, Transaction transaction);
    void enregistrerTransactionDansDB(Transaction transaction, Compte compte) throws SQLException;

    List<Transaction> getTransactionsBeforeDateTime(int compteId, LocalDateTime dateTime);
    double getSoldeAtDateTime(Compte compte, LocalDateTime dateTime);
    List<Double> getSoldeHistoryInDateTimeRange(Compte compte, LocalDateTime startDate, LocalDateTime endDate);
}
