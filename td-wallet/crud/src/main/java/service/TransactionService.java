package service;

import interfaceGenerique.transactionOperation;
import model.Compte;
import model.Transaction;
import model.TransferHistory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionService implements transactionOperation {

    private Connection connection;

    public TransactionService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Transaction create(Transaction toInsert) {
        String query = "INSERT INTO transaction (label, montant, date_heure, type_transaction) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, toInsert.getLabel());
            preparedStatement.setDouble(2, toInsert.getMontant());
            preparedStatement.setObject(3, toInsert.getDateHeure());
            preparedStatement.setBoolean(4, toInsert.isType_transaction());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                toInsert.setId_transaction(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toInsert;
    }

    @Override
    public List<Transaction> readAll() throws SQLException {
        List<Transaction> allTransactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                allTransactions.add(convertToTransaction(result));
            }
        }
        return allTransactions;
    }

    @Override
    public Transaction delete(Transaction toDelete) throws SQLException {
        String sql = "DELETE FROM transaction WHERE id_transaction = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, toDelete.getId_transaction());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("DELETE transaction");
        return toDelete;
    }

    @Override
    public Transaction update(Transaction existingTransaction, Transaction newTransactionDetails) {
        String sql = "UPDATE transaction SET label = ?, montant = ?, dateHeure = ?, type_transaction = ? WHERE id_transaction = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newTransactionDetails.getLabel());
            statement.setDouble(2, newTransactionDetails.getMontant());
            statement.setObject(3, newTransactionDetails.getDateHeure());
            statement.setBoolean(4, newTransactionDetails.isType_transaction());
            statement.setInt(5, existingTransaction.getId_transaction());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                existingTransaction.setLabel(newTransactionDetails.getLabel());
                existingTransaction.setMontant(newTransactionDetails.getMontant());
                existingTransaction.setDateHeure(newTransactionDetails.getDateHeure());
                existingTransaction.setType_transaction(newTransactionDetails.isType_transaction());

                System.out.println("La transaction a été mise à jour avec succès.");
            } else {
                System.out.println("Aucune mise à jour effectuée. Vérifiez l'ID de la transaction.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existingTransaction;
    }

    @Override
    public Compte effectuerTransaction(Compte compte, Transaction transaction) {
        transaction.setDateHeure(LocalDateTime.now());

        double montantTransaction = transaction.isType_transaction() ? transaction.getMontant() : -transaction.getMontant();

        try {
            if ("Banque".equals(compte.getType()) || compte.getSolde() + montantTransaction >= 0) {
                compte.setSolde(compte.getSolde() + montantTransaction);
                compte.getTransactions().add(transaction);

                enregistrerTransactionDansDB(transaction, compte);
            } else {
                System.out.println("Solde insuffisant pour effectuer la transaction.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compte;
    }

    @Override
    public void enregistrerTransactionDansDB(Transaction transaction, Compte compte) throws SQLException {
        String query = "INSERT INTO transaction (id_compte, label, montant, dateHeure, type_transaction) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, compte.getId_compte());
            preparedStatement.setString(2, transaction.getLabel());
            preparedStatement.setDouble(3, transaction.getMontant());
            preparedStatement.setObject(4, transaction.getDateHeure());
            preparedStatement.setBoolean(5, transaction.isType_transaction());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                transaction.setId_transaction(generatedKeys.getInt(1));
            }
        }

        String updateQuery = "UPDATE compte SET solde = ? WHERE id_compte = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setDouble(1, compte.getSolde());
            updateStatement.setInt(2, compte.getId_compte());
            updateStatement.executeUpdate();
        }
    }

    @Override
    public double getSoldeAtDateTime(Compte compte, LocalDateTime dateTime) {
        double solde = 0;

        List<Transaction> transactions = getTransactionsBeforeDateTime(compte.getId_compte(), dateTime);

        for (Transaction transaction : transactions) {
            if (transaction.getDateHeure().isAfter(dateTime)) {
                break;
            }

            if (transaction.isType_transaction()) {
                solde += transaction.getMontant();
            } else {
                solde -= transaction.getMontant();
            }
        }

        return solde;
    }

    @Override
    public List<Transaction> getTransactionsBeforeDateTime(int compteId, LocalDateTime dateTime) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE id_compte = ? AND dateHeure <= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, compteId);
            preparedStatement.setObject(2, dateTime);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                transactions.add(convertToTransaction(result));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Double> getSoldeHistoryInDateTimeRange(Compte compte, LocalDateTime startDate, LocalDateTime endDate) {
        List<Double> soldeHistory = new ArrayList<>();

        // Récupérer les transactions dans l'intervalle de date spécifiée
        List<Transaction> transactions = getTransactionsInDateTimeRange(compte.getId_compte(), startDate, endDate);

        // Calculer le solde pour chaque instant de temps dans l'intervalle
        for (LocalDateTime dateTime = startDate; dateTime.isBefore(endDate); dateTime = dateTime.plusSeconds(1)) {
            double solde = getSoldeAtDateTime(compte, dateTime);
            soldeHistory.add(solde);
        }

        return soldeHistory;
    }
    @Override
    public List<Transaction> getTransactionsInDateTimeRange(int compteId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE id_compte = ? AND dateHeure >= ? AND dateHeure <= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, compteId);
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                transactions.add(convertToTransaction(result));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void effectuerTransfert(Compte compteDebiteur, Compte compteCrediteur, double montant) {
        if (compteDebiteur.equals(compteCrediteur)) {
            System.out.println("Impossible de transférer de l'argent vers le même compte.");
            return;
        }

        Transaction transactionDebiteur = new Transaction(compteDebiteur.getId_compte(), "Transfert sortant", montant, LocalDateTime.now(), false);
        Transaction transactionCrediteur = new Transaction(compteCrediteur.getId_compte(), "Transfert entrant", montant, LocalDateTime.now(), true);

        try {
            connection.setAutoCommit(false);

            if ("Banque".equals(compteDebiteur.getType()) || compteDebiteur.getSolde() - montant >= 0) {
                compteDebiteur.setSolde(compteDebiteur.getSolde() - montant);
                compteDebiteur.getTransactions().add(transactionDebiteur);
                enregistrerTransactionDansDB(transactionDebiteur, compteDebiteur);

                compteCrediteur.setSolde(compteCrediteur.getSolde() + montant);
                compteCrediteur.getTransactions().add(transactionCrediteur);
                enregistrerTransactionDansDB(transactionCrediteur, compteCrediteur);

                enregistrerTransfertDansHistorique(transactionDebiteur.getId_transaction(), transactionCrediteur.getId_transaction());

                connection.commit();
            } else {
                System.out.println("Solde insuffisant pour effectuer le transfert.");
                connection.rollback();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<TransferHistory> getTransfertHistoryInDateTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<TransferHistory> transferHistoryList = new ArrayList<>();

        String sql = "SELECT * FROM TransferHistory WHERE dateTransfert BETWEEN ? AND ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, startDate);
            preparedStatement.setObject(2, endDate);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                TransferHistory transferHistory = new TransferHistory();
                transferHistory.setId(result.getInt("id"));
                transferHistory.setIdTransactionDebiteur(result.getInt("id_transaction_debiteur"));
                transferHistory.setIdTransactionCrediteur(result.getInt("id_transaction_crediteur"));
                transferHistory.setDateTransfert(result.getObject("dateTransfert", LocalDateTime.class));

                transferHistoryList.add(transferHistory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transferHistoryList;
    }

    private void enregistrerTransfertDansHistorique(int idTransactionDebiteur, int idTransactionCrediteur) throws SQLException {
        String query = "INSERT INTO TransferHistory (id_transaction_debiteur, id_transaction_crediteur, dateTransfert) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idTransactionDebiteur);
            preparedStatement.setInt(2, idTransactionCrediteur);
            preparedStatement.setObject(3, LocalDateTime.now());
            preparedStatement.executeUpdate();
        }
    }




    private Transaction convertToTransaction(ResultSet result) throws SQLException {
        int id_transaction = result.getInt("id_transaction");
        String label = result.getString("label");
        double montant = result.getDouble("montant");
        LocalDateTime dateHeure = result.getObject("dateHeure", LocalDateTime.class);
        boolean type_transaction = result.getBoolean("type_transaction");

        return new Transaction(id_transaction, label, montant, dateHeure, type_transaction);
    }

}
