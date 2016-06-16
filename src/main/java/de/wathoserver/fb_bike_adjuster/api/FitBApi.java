package de.wathoserver.fb_bike_adjuster.api;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
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
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import de.wathoserver.fb_bike_adjuster.oauth2.MyDataStoreCredentialRefreshListener;
import de.wathoserver.fb_bike_adjuster.oauth2.OAuth2ClientConfig;

@Component
public class FitBApi {

  private static final Logger log = LoggerFactory.getLogger(FitBApi.class);

  public static final String LOCAL_DATE_PATTERN = "yyyy-MM-dd";
  public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
  public static final String TIME_UPDATED_PATTERN = DATE_TIME_PATTERN;
  public static final String LOCAL_TIME_HOURS_MINUTES_PATTERN = "HH:mm";
  public static final String LOCAL_TIME_HOURS_MINUTES_SECONDS_PATTERN = "HH:mm:ss";

  public static final DateTimeZone SERVER_TIME_ZONE = DateTimeZone.getDefault();
  public static final DateTimeFormatter LOCAL_DATE_FORMATTER =
      DateTimeFormat.forPattern(LOCAL_DATE_PATTERN).withZone(SERVER_TIME_ZONE);
  public static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormat.forPattern(DATE_TIME_PATTERN).withZone(SERVER_TIME_ZONE);
  public static final DateTimeFormatter TIME_UPDATED_FORMATTER =
      DateTimeFormat.forPattern(TIME_UPDATED_PATTERN).withZone(SERVER_TIME_ZONE);
  public static final DateTimeFormatter LOCAL_TIME_HOURS_MINUTES_FORMATTER =
      DateTimeFormat.forPattern(LOCAL_TIME_HOURS_MINUTES_PATTERN);
  public static final DateTimeFormatter LOCAL_TIME_HOURS_MINUTES_SECONDS_FORMATTER =
      DateTimeFormat.forPattern(LOCAL_TIME_HOURS_MINUTES_SECONDS_PATTERN);


  @Autowired
  OAuth2ClientConfig config;

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

  private HttpRequestFactory requestFactory;

  public void logSettings() {
    log.debug("OAuthClientSettings: key={}, secret={}, domain={}, port={}", config.getApiKey(),
        config.getApiSecret(), config.getDomain(), config.getPort());
  }

  private void init() throws Exception {
    DATA_STORE_FACTORY = new FileDataStoreFactory(new File("cache"));
    DataStore<StoredCredential> defaultDataStore =
        StoredCredential.getDefaultDataStore(DATA_STORE_FACTORY);
    StoredCredential storedCredential = defaultDataStore.get("watho81");
    log.debug("Stored Credential: {}", storedCredential);
    final Credential credential = authorize("watho81");
    requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
      @Override
      public void initialize(HttpRequest request) throws IOException {
        credential.initialize(request);
        request.setParser(new JsonObjectParser(JSON_FACTORY));
      }
    });
  }

  /** Authorizes the installed application to access user's protected data. */
  private Credential authorize(String user) throws Exception {
    // set up authorization code flow
    BasicAuthentication basicAuthentication =
        new BasicAuthentication(config.getApiKey(), config.getApiSecret());
    DataStore<StoredCredential> defaultDataStore =
        StoredCredential.getDefaultDataStore(DATA_STORE_FACTORY);
    AuthorizationCodeFlow flow =
        new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
            HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(TOKEN_SERVER_URL), basicAuthentication,
            config.getApiKey(), AUTHORIZATION_SERVER_URL).setScopes(Arrays.asList(SCOPE))
                .setDataStoreFactory(DATA_STORE_FACTORY).setCredentialDataStore(defaultDataStore)
                .addRefreshListener(
                    new MyDataStoreCredentialRefreshListener(user, defaultDataStore))
                .build();
    // authorize
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost(config.getDomain())
        .setPort(config.getPort()).build();
    AuthorizationCodeInstalledApp authorizationCodeInstalledApp =
        new AuthorizationCodeInstalledApp(flow, receiver);
    Credential credentials = authorizationCodeInstalledApp.authorize(user);

    log.debug("Retrieved Credentials: " + credentials.toString());
    return credentials;
  }

  public HttpRequestFactory getRequestFactory() throws Exception {
    if (requestFactory == null) {
      init();
    }
    return requestFactory;
  }

  public <T> T executeGetRequest(GenericUrl fitBUrl, Class<T> dataClazz) throws Exception {
    HttpRequest request = getRequestFactory().buildGetRequest(fitBUrl);
    return request.execute().parseAs(dataClazz);
  }

  public <T> T executePostRequest(GenericUrl fitBUrl, Class<T> dataClazz) throws Exception {
    HttpRequest request = getRequestFactory().buildPostRequest(fitBUrl, null);
    return request.execute().parseAs(dataClazz);
  }
}
