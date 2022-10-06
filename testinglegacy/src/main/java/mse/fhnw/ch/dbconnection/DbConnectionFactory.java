package mse.fhnw.ch.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnectionFactory implements IDbConnectionFactory {

    /**
     * @return DBConnection or null if connection can't be established
     */
    public Connection createDefaultConnection() {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "policydb";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "policyapp";
        String password = "prodpass";

        Connection connection = null;
        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(url + dbName, userName, password);
            connection.close();
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
