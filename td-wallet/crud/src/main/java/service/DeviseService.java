package service;

import interfaceGenerique.deviseOperation;
import model.Devise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviseService implements deviseOperation {

    private Connection connection;

    public DeviseService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Devise create(Devise toInsert) {
        String query = "INSERT INTO devise (nom_devise, code_devise) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, toInsert.getNom());
            preparedStatement.setString(2, toInsert.getCode());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                toInsert.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toInsert;
    }

    @Override
    public List<Devise> readAll() throws SQLException {
        List<Devise> allDevises = new ArrayList<>();
        String sql = "SELECT * FROM Devise";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                allDevises.add(convertToDevise(result));
            }
        }
        return allDevises;
    }

    @Override
    public Devise delete(Devise toDelete) throws SQLException {
        String sql = "DELETE FROM Devise WHERE ID_Devise = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, toDelete.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("DELETE Devise");
        return toDelete;
    }

    @Override
    public Devise update(Devise existingDevise, Devise newDeviseDetails) {
        existingDevise.setNom(newDeviseDetails.getNom());

        String sql = "UPDATE Devise SET Nom_Devise = ? WHERE ID_Devise = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, existingDevise.getNom());
            preparedStatement.setInt(2, existingDevise.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return existingDevise;
    }

    private Devise convertToDevise(ResultSet result) throws SQLException {
        int idDevise = result.getInt("id_devise");
        String nomDevise = result.getString("nom_devise");
        String code = result.getString("code_devise");

        return new Devise(idDevise, nomDevise , code);
    }

}
