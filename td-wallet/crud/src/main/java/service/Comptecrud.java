package service;

import interfaceGenerique.crudOperation;
import lombok.AllArgsConstructor;
import model.Compte;
import model.Devise;
import model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Comptecrud implements crudOperation {

    private Connection connection;

    @Override
    public Compte insert(Compte toInsert) {
        String query = "INSERT INTO compte (id_compte, nom, solde, devise, type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, toInsert.getId_compte());
            preparedStatement.setString(2, toInsert.getNom());
            preparedStatement.setInt(3, toInsert.getSolde());
            preparedStatement.setString(4, toInsert.getDevise().getNom()); // Modifie selon la structure de ta base de données
            preparedStatement.setString(5, toInsert.getType());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toInsert;
    }

    @Override
    public List<Compte> findAll() throws SQLException {
        List<Compte> allCompte = new ArrayList<>();
        String sql = "SELECT * FROM compte";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                allCompte.add(convertToCompte(result));
            }
        }
        return allCompte;
    }

    private Compte convertToCompte(ResultSet result) throws SQLException {
        int id_compte = result.getInt("id_compte");
        String nom = result.getString("nom");
        Integer solde = result.getInt("solde");
        Devise devise = new Devise(result.getInt("id_devise"), result.getString("nom_devise"), result.getString("code_devise"));
        String type = result.getString("type");

        return new Compte(id_compte, nom, solde, devise, type);
    }

    @Override
    public Compte delete(Compte toDelete) throws SQLException {
        String sql = "DELETE FROM compte WHERE id_compte = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, toDelete.getId_compte());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("DELETE 01");
        return toDelete;
    }
}
