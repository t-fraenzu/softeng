package mse.fhnw.ch.repositories;

public interface IPolicyRepository {

    SavedPolicy getSavedPolicyByCustomerId(String customerId) throws DataFetchException;
}
