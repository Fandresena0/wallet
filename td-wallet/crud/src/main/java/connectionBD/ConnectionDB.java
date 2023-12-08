package connectionBD;

import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
@Bean
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                System.getenv("url"),
                System.getenv("user"),
                System.getenv("password")
        );
    }
}
