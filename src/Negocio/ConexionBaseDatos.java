package Negocio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDatos {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=PEDIDOS;encrypt=false";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
      return DriverManager.getConnection(URL,USERNAME, PASSWORD);
    }
}