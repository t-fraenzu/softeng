package mse.fhnw.ch;

import mse.fhnw.ch.adapter.ITierUtilAdapter;
import mse.fhnw.ch.rating.RatingStrategyResolver;
import mse.fhnw.ch.repositories.DataFetchException;
import mse.fhnw.ch.repositories.IPolicyRepository;
import mse.fhnw.ch.repositories.SavedPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerTest {


    private Customer testee;
    private IPolicyRepository policyRepository;
    private ITierUtilAdapter tierUtilAdapter;
    private RatingStrategyResolver ratingStrategyResolver;

    @BeforeEach
    public void SetUp() {
        policyRepository = mock(IPolicyRepository.class);
        ratingStrategyResolver = mock(RatingStrategyResolver.class);
        tierUtilAdapter = mock(ITierUtilAdapter.class);
        testee = new Customer(policyRepository, ratingStrategyResolver, tierUtilAdapter);
    }

    @Test
    public void test_ratePolice_mapsBestandPolice() throws DataFetchException {
        String policyId = "policyId";
        Policy policy = new Policy();
        policy.setId(policyId);

        SavedPolicy savedPolicy = new SavedPolicy();
        savedPolicy.setScore("score");
        savedPolicy.setBirthYear("2017");
        savedPolicy.setName1("Name1");
        savedPolicy.setName2("Name2");
        savedPolicy.setState("state");

        Rater rater = mock(Rater.class);
        Object tierUtilResponse = new Object();
        when(tierUtilAdapter.assignTier(savedPolicy.getBirthYear(), savedPolicy.getScore()))
                .thenReturn(tierUtilResponse);
        Object expectedRate = new Object();
        when(rater.rate(savedPolicy.getState(), tierUtilResponse)).thenReturn(expectedRate);
        when(ratingStrategyResolver.resolveRaterForPolicy(policy)).thenReturn(rater);
        when(policyRepository.getSavedPolicyByCustId(policyId)).thenReturn(savedPolicy);

        // act
        Policy erweitertePolicy = testee.ratePolicy(policy);

        // assert
        assertEquals(savedPolicy.getName2() + savedPolicy.getName1(), erweitertePolicy.getLastName());
        assertEquals(expectedRate, erweitertePolicy.getRate());
        assertEquals(expectedRate, erweitertePolicy.getRate());
    }
}