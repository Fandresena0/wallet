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
public class Comptecrud implements crudOperation{
    private Connection connection;
    @Override
    public Compte insert(Compte toInsert) {
        String query = "INSERT INTO compte (id_compte, nome_utilisateur, transaction,devisse) VALUES (?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, toInsert.getId_compte());
            preparedStatement.setString(2, toInsert.getNom_utilisateur());
            preparedStatement.setString(3, String.valueOf(toInsert.getTransaction()));
            preparedStatement.setString(4,String.valueOf(toInsert.getDevise()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toInsert;
    }

    @Override
    public List<Compte> findAll() throws SQLException {
        List<Compte> allCompte = new ArrayList<>();
        String sql="SELECT * FROM compte";
        try (PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            ResultSet result = preparedStatement.executeQuery();
            convertToList(allCompte,result);
        }
        return allCompte;
    }

    private void convertToList(List<Compte> allCompte, ResultSet result) throws SQLException{
       allCompte.add(new Compte(
               result.getInt("id_compte"),
               result.getString("nome_utilisateur"),
               (Transaction) result.getObject("transaction"),
               (Devise) result.getObject("devise")
       ));
    }

    @Override
    public Compte delete(Compte toDelete) throws SQLException {
        String sql = "DELETE FROM compte WHERE id_compte = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, toDelete.getId_compte());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("DELETE 01");
        return toDelete;
    }


}
