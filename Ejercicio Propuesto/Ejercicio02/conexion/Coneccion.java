package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Coneccion {
    private static final String URL = "jdbc:mysql://localhost:3308/empresa";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    // Método de prueba
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            System.out.println("Conexión exitosa a MySQL!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }
}

