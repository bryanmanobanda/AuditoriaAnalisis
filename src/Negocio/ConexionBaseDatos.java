package Negocio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDatos {
    private static final String SERVER = "jdbc:sqlserver://localhost:1433;databaseName=";
    private static final String ENCRYPT = ";encrypt=false";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "root";
    private static String URL = "";

    public static Connection getConnection(String BD) throws SQLException {
      URL = SERVER + BD + ENCRYPT;
      return DriverManager.getConnection(URL,USERNAME, PASSWORD);
    }
}