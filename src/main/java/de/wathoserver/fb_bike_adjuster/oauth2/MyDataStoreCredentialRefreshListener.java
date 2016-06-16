package de.wathoserver.fb_bike_adjuster.oauth2;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;

public class MyDataStoreCredentialRefreshListener implements CredentialRefreshListener {
  private static final Logger log =
      LoggerFactory.getLogger(MyDataStoreCredentialRefreshListener.class);


  /** Stored credential data store. */
  private final DataStore<StoredCredential> credentialDataStore;

  /** User ID whose credential is to be updated. */
  private final String userId;

  /**
   * Constructor using {@link StoredCredential#getDefaultDataStore(DataStoreFactory)} for the stored
   * credential data store.
   *
   * @param userId user ID whose credential is to be updated
   * @param dataStoreFactory data store factory
   */
  public MyDataStoreCredentialRefreshListener(String userId, DataStoreFactory dataStoreFactory)
      throws IOException {
    this(userId, StoredCredential.getDefaultDataStore(dataStoreFactory));
  }

  /**
   * @param userId user ID whose credential is to be updated
   * @param credentialDataStore stored credential data store
   */
  public MyDataStoreCredentialRefreshListener(String userId,
      DataStore<StoredCredential> credentialDataStore) {
    this.userId = Preconditions.checkNotNull(userId);
    this.credentialDataStore = Preconditions.checkNotNull(credentialDataStore);
  }

  @Override
  public void onTokenResponse(Credential credential, TokenResponse tokenResponse)
      throws IOException {
    makePersistent(credential);
  }

  @Override
  public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse)
      throws IOException {
    clear(credential);
  }

  /** Returns the stored credential data store. */
  public DataStore<StoredCredential> getCredentialDataStore() {
    return credentialDataStore;
  }

  /** Stores the updated credential in the credential store. */
  public void makePersistent(Credential credential) throws IOException {
    credentialDataStore.set(userId, new StoredCredential(credential));
  }

  /**
   * Removes the credential from the credential store.
   *
   * @param credential
   * @throws IOException
   */
  public void clear(Credential credential) throws IOException {
    credentialDataStore.delete(userId);
  }
}
