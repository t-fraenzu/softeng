package mse.fhnw.ch.rating;

import mse.fhnw.ch.CARater;
import mse.fhnw.ch.NYRater;
import mse.fhnw.ch.Policy;
import mse.fhnw.ch.Rater;

public class RatingStrategyResolver {

    /**
     * @param policy of with information are used to decide which rater should be taken
     * @return rater for given policy. NULL if no rater is defined for policy
     */
    public Rater resolveRaterForPolicy(Policy policy) {
        Rater rater = null;
        if (policy.getState().equals("NY")) {
            rater = new NYRater();
        } else if (policy.getState().equals("CA")) {
            rater = new CARater();
        }

        return rater;
    }
}
