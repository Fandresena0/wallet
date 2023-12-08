package service;

import interfaceGenerique.crudOperation;
import lombok.AllArgsConstructor;
import model.Compte;
import model.Devise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CompteService implements crudOperation {

    private Connection connection;

    @Override
    public Compte insert(Compte toInsert) {
        String query = "INSERT INTO compte (nom_compte, solde, id_devise, type_compte) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, toInsert.getNom());
            preparedStatement.setDouble(2, toInsert.getSolde());
            preparedStatement.setInt(3, toInsert.getDevise().getId()); // Correction selon la structure de la table devise
            preparedStatement.setString(4, toInsert.getType());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                toInsert.setId_compte(generatedKeys.getInt(1));
            }
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
        Double solde = result.getDouble("solde");
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

    @Override
    public Compte updateCompte(Compte existingCompte, Compte newCompteDetails) {
        String sql = "UPDATE compte SET nom = ?, type = ?, devise = ? WHERE id_compte = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newCompteDetails.getNom());
            statement.setString(2, newCompteDetails.getType());
            statement.setInt(3, newCompteDetails.getDevise().getId());
            statement.setInt(4, existingCompte.getId_compte());


            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                existingCompte.setNom(newCompteDetails.getNom());
                existingCompte.setType(newCompteDetails.getType());
                existingCompte.setDevise(newCompteDetails.getDevise());

                System.out.println("Le compte a été mis à jour avec succès.");
            } else {
                System.out.println("Aucune mise à jour effectuée. Vérifiez l'ID du compte.");
            }
            return existingCompte;
        } catch (SQLException e) {
            e.printStackTrace();

            return existingCompte;
        }
    }


}
