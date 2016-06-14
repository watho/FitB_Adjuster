package de.wathoserver.fb_bike_adjuster.oauth2;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import de.wathoserver.fb_bike_adjuster.model.Activities;
import de.wathoserver.fb_bike_adjuster.model.Activity;

public class OAuth2_Test {

  private static final org.slf4j.Logger log = LoggerFactory.getLogger(OAuth2_Test.class);

  /**
   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
   * globally shared instance across your application.
   */
  private static FileDataStoreFactory DATA_STORE_FACTORY;

  /** OAuth 2 scope. */
  private static final String SCOPE = "activity";

  /** Global instance of the HTTP transport. */
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  /** Global instance of the JSON factory. */
  static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private static final String TOKEN_SERVER_URL = "https://api.fitbit.com/oauth2/token";
  private static final String AUTHORIZATION_SERVER_URL = "https://www.fitbit.com/oauth2/authorize";

  @Test
  public void testOAuth2() throws Exception {
    Logger.getLogger(HttpTransport.class.getName()).setLevel(Level.ALL);
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    DATA_STORE_FACTORY = new FileDataStoreFactory(new File(""));
    final Credential credential = authorize();
    HttpRequestFactory requestFactory =
        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
          @Override
          public void initialize(HttpRequest request) throws IOException {
            credential.initialize(request);
            request.setParser(new JsonObjectParser(JSON_FACTORY));
          }
        });
    assertThat(credential.getAccessToken(), is(notNullValue()));
    log.debug(credential.getAccessToken());

    FitBUrl getActivitiesAfterDate = new FitBUrl();
    getActivitiesAfterDate.setAfterDate("2016-06-13");
    HttpRequest request = requestFactory.buildGetRequest(getActivitiesAfterDate);
    Activities activities = request.execute().parseAs(Activities.class);
    assertThat(activities, is(notNullValue()));
    assertThat(activities.getActivities(), is(notNullValue()));
    assertThat(activities.getActivities().size(), is(greaterThan(0)));
    for (Activity activity : activities.getActivities()) {
      log.debug("activity found = {}. Duration={},all={}", activity.getActivityName(),
          activity.getActiveDuration(), activity);
    }
  }

  /** Authorizes the installed application to access user's protected data. */
  private static Credential authorize() throws Exception {
    OAuth2ClientCredentials.errorIfNotSpecified();
    // set up authorization code flow
    // ClientParametersAuthentication clientAuthentication = new ClientParametersAuthentication(
    // OAuth2ClientCredentials.API_KEY, OAuth2ClientCredentials.API_SECRET);
    BasicAuthentication basicAuthentication = new BasicAuthentication(
        OAuth2ClientCredentials.API_KEY, OAuth2ClientCredentials.API_SECRET);
    AuthorizationCodeFlow flow =
        new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
            HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(TOKEN_SERVER_URL), basicAuthentication,
            OAuth2ClientCredentials.API_KEY, AUTHORIZATION_SERVER_URL)
                .setScopes(Arrays.asList(SCOPE)).setDataStoreFactory(DATA_STORE_FACTORY).build();
    // authorize
    LocalServerReceiver receiver = new LocalServerReceiver.Builder()
        .setHost(OAuth2ClientCredentials.DOMAIN).setPort(OAuth2ClientCredentials.PORT).build();
    AuthorizationCodeInstalledApp authorizationCodeInstalledApp =
        new AuthorizationCodeInstalledApp(flow, receiver);
    Credential credentials = authorizationCodeInstalledApp.authorize("watho81");

    return credentials;
  }
}
