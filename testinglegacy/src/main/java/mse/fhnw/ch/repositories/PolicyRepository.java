package mse.fhnw.ch.repositories;

import mse.fhnw.ch.dbconnection.IDbConnectionFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PolicyRepository {

    private final IDbConnectionFactory dbConnectionFactory;

    public PolicyRepository(IDbConnectionFactory dbConnectionFactory){

        this.dbConnectionFactory = dbConnectionFactory;
    }

    public SavedPolicy getSavedPolicyByCustomerId(String customerId) throws DataFetchException {
        try {
            Statement statement = dbConnectionFactory.createDefaultConnection().createStatement();
            ResultSet srs = statement.executeQuery("SELECT NAME2, NAME1, BIRTHYEAR, SCORE, STATE FROM POLICIES WHERE CUST=" + customerId);
            SavedPolicy savedPolicy = new SavedPolicy();
            savedPolicy.setName1(srs.getString("NAME1"));
            savedPolicy.setName2(srs.getString("NAME2"));
            savedPolicy.setState(srs.getString("STATE"));
            savedPolicy.setBirthYear(srs.getString("BIRTHYEAR"));
            savedPolicy.setScore(srs.getString("SCORE"));
            return savedPolicy;
        } catch (SQLException e) {
            throw new DataFetchException();
        }
    }

}
