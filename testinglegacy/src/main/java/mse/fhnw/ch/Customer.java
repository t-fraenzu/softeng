package mse.fhnw.ch;

import mse.fhnw.ch.dbconnection.DbConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer {

    public Customer(RGHConnection con) {
    }

    public Policy ratePolicy(Policy policy, Customer customer) throws SQLException {
        Rater rater = null;
        if (policy.getState().equals("NY")) {
            rater = new NYRater();
        } else if (policy.getState().equals("CA")) {
            rater = new CARater();
        }
        TierUtil tierUtil = new TierUtil(); // Note: This makes a Web Services
        // call under the covers
        Connection connection = new DbConnectionFactory().createDefaultConnection();
        Statement stmt = connection.createStatement();
        ResultSet srs = stmt.executeQuery("SELECT NAME2, NAME1, BIRTHYEAR, SCORE, STATE FROM POLICIES WHERE CUST=" + policy.getId());
        policy.setLastName(srs.getString("NAME2"));
        policy.setLastName(srs.getString("NAME1"));
        policy.setRate(rater.rate(srs.getString("STATE"),
                tierUtil.assignTier(srs.getString("BIRTHYEAR"), srs.getString("SCORE"))));

        return policy;
    }
}


