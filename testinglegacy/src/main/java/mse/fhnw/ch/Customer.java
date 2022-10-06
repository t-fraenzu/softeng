package mse.fhnw.ch;

import mse.fhnw.ch.dbconnection.DbConnectionFactory;
import mse.fhnw.ch.repositories.DataFetchException;
import mse.fhnw.ch.repositories.IPolicyRepository;
import mse.fhnw.ch.repositories.SavedPolicy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer {

    private final IPolicyRepository policyRepository;

    public Customer(IPolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public Policy ratePolicy(Policy policy, Customer customer) throws DataFetchException {
        Rater rater = null;
        if (policy.getState().equals("NY")) {
            rater = new NYRater();
        } else if (policy.getState().equals("CA")) {
            rater = new CARater();
        }
        TierUtil tierUtil = new TierUtil(); // Note: This makes a Web Services
        // call under the covers
        SavedPolicy savedPolicy = policyRepository.getSavedPolicyByCustomerId(policy.getId());
        policy.setLastName(savedPolicy.getName2());
        policy.setLastName(savedPolicy.getName1());
        policy.setRate(rater.rate(savedPolicy.getState(),
                tierUtil.assignTier(savedPolicy.getBirthYear(), savedPolicy.getScore())));

        return policy;
    }
}


