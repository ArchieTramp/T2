package ru.tele2.education.connection;

import javax.ejb.EJB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@EJB
public class ConnectionManagerImpl implements ConnectionManager{

    private final String url ="jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String pass = "admin";

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
