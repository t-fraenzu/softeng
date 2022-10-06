package mse.fhnw.ch.repositories;

public interface IPolicyRepository {

    SavedPolicy getSavedPolicyByCustId(String customerId) throws DataFetchException;
}
