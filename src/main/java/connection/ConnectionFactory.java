package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        String sql = "jdbc:mysql://localhost:3306/estudos_java";
        String username = "root";
        String password = "root";
        return DriverManager.getConnection(sql, username, password);
    }
}
