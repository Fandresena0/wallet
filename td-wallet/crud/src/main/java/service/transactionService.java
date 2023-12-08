package service;

import interfaceGenerique.crudOperation;
import interfaceGenerique.transactionOperation;
import model.Compte;
import model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class transactionService implements transactionOperation {

    private Connection connection;

    public transactionService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Transaction create(Transaction toInsert) {
        String query = "INSERT INTO transaction (id_compte, label, montant, dateHeure, type_transaction) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, toInsert.getId_transaction());
            preparedStatement.setString(2, toInsert.getLabel());
            preparedStatement.setDouble(3, toInsert.getMontant());
            preparedStatement.setObject(4, toInsert.getDateHeure());
            preparedStatement.setBoolean(5, toInsert.isType_transaction());
            preparedStatement.executeUpdate();
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
        // Mettre à jour les attributs de la transaction avec les nouvelles informations
        existingTransaction.setLabel(newTransactionDetails.getLabel());
        existingTransaction.setMontant(newTransactionDetails.getMontant());
        existingTransaction.setDateHeure(newTransactionDetails.getDateHeure());
        existingTransaction.setType_transaction(newTransactionDetails.isType_transaction());

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
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, compte.getId_compte());
            preparedStatement.setString(2, transaction.getLabel());
            preparedStatement.setDouble(3, transaction.getMontant());
            preparedStatement.setObject(4, transaction.getDateHeure());
            preparedStatement.setBoolean(5, transaction.isType_transaction());
            preparedStatement.executeUpdate();
        }

        String updateQuery = "UPDATE compte SET solde = ? WHERE id_compte = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setDouble(1, compte.getSolde());
            updateStatement.setInt(2, compte.getId_compte());
            updateStatement.executeUpdate();
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
