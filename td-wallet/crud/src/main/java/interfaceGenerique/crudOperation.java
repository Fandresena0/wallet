package interfaceGenerique;

import model.Compte;

import java.sql.SQLException;
import java.util.List;

public interface crudOperation {

    Compte insert(Compte toInsert);
    List<Compte> findAll() throws SQLException;
    Compte delete(Compte toDelete) throws SQLException;
}
