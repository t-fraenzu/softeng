package mse.fhnw.ch;

import mse.fhnw.ch.adapter.ITierUtilAdapter;
import mse.fhnw.ch.rating.RatingStrategyResolver;
import mse.fhnw.ch.repositories.DataFetchException;
import mse.fhnw.ch.repositories.IPolicyRepository;
import mse.fhnw.ch.repositories.SavedPolicy;

public class Customer {

    private final IPolicyRepository policyRepository;
    private final RatingStrategyResolver ratingStrategyResolver;
    private final ITierUtilAdapter tierUtilAdapter;

    public Customer(IPolicyRepository policyRepository, RatingStrategyResolver ratingStrategyResolver, ITierUtilAdapter tierUtilAdapter) {
        this.policyRepository = policyRepository;
        this.ratingStrategyResolver = ratingStrategyResolver;
        this.tierUtilAdapter = tierUtilAdapter;
    }

    public Policy ratePolicy(Policy policy) throws DataFetchException {
        Rater rater = ratingStrategyResolver.resolveRaterForPolicy(policy);
        // call under the covers
        SavedPolicy savedPolicy = policyRepository.getSavedPolicyByCustId(policy.getId());
        policy.setLastName(savedPolicy.getName2());
        policy.setLastName(savedPolicy.getName1());
        policy.setRate(rater.rate(savedPolicy.getState(),
                tierUtilAdapter.assignTier(savedPolicy.getBirthYear(), savedPolicy.getScore())));

        return policy;
    }
}


