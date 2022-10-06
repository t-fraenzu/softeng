package mse.fhnw.ch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer{

    private Connection connection;

    public Customer(RGHConnection con)
    {
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
   String url = "jdbc:mysql://localhost:3306/";
   String dbName = "policydb";
   String driver = "com.mysql.jdbc.Driver";
   String userName = "policyapp"; 
   String password = "prodpass";
   try {
     Class.forName(driver).newInstance();
     connection = DriverManager.getConnection(url+dbName,userName,password);
     connection.close();
} catch (Exception e) {
     e.printStackTrace();
     return null;
}
   Statement stmt = connection.createStatement();
   ResultSet srs = stmt.executeQuery( "SELECT NAME2, NAME1, BIRTHYEAR, SCORE, STATE FROM POLICIES WHERE CUST=" + policy.getId());
   policy.setLastName(srs.getString("NAME2"));
   policy.setLastName(srs.getString("NAME1"));
   policy.setRate(rater.rate(srs.getString("STATE"), 
   tierUtil.assignTier(srs.getString("BIRTHYEAR"), srs.getString("SCORE"))));

   return policy;
}
}


