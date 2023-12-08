package interfaceGenerique;

import model.Devise;

import java.sql.SQLException;
import java.util.List;

public interface deviseOperation {
    Devise create(Devise toInsert);
    List<Devise> readAll() throws SQLException;
    Devise delete(Devise toDelete) throws SQLException;
    Devise update(Devise existingDevise, Devise newDeviseDetails);
}
