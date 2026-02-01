import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // JDBC connection URL and database credentials
    static final String SOURCEURL = "jdbc:mysql://localhost/usuario?allowPublicKeyRetrieval=true&useSSL=false";
    static final String USER = "alumno";
    static final String PASSWORD = "mipassword";

    // Establish the connecion to the MYSQL database
    public static Connection getConnection() throws SQLException {
        return (Connection) DriverManager.getConnection(SOURCEURL, USER, PASSWORD);
    }
}
