package mse.fhnw.ch.dbconnection;

import java.sql.Connection;

public interface IDbConnectionFactory {

    /**
     * @return DBConnection or null if connection can't be established
     */
    Connection createDefaultConnection();
}
