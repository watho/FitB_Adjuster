package de.wathoserver.fb_bike_adjuster.oauth2;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Component
public class FitBApi {

  private static final Logger log = LoggerFactory.getLogger(FitBApi.class);

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
    final Credential credential = authorize();
    requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
      @Override
      public void initialize(HttpRequest request) throws IOException {
        credential.initialize(request);
        request.setParser(new JsonObjectParser(JSON_FACTORY));
      }
    });
  }

  /** Authorizes the installed application to access user's protected data. */
  private Credential authorize() throws Exception {
    // set up authorization code flow
    // ClientParametersAuthentication clientAuthentication = new ClientParametersAuthentication(
    // OAuth2ClientCredentials.API_KEY, OAuth2ClientCredentials.API_SECRET);
    BasicAuthentication basicAuthentication =
        new BasicAuthentication(config.getApiKey(), config.getApiSecret());
    AuthorizationCodeFlow flow =
        new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
            HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(TOKEN_SERVER_URL), basicAuthentication,
            config.getApiKey(), AUTHORIZATION_SERVER_URL).setScopes(Arrays.asList(SCOPE))
                .setDataStoreFactory(DATA_STORE_FACTORY).build();
    // authorize
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost(config.getDomain())
        .setPort(config.getPort()).build();
    AuthorizationCodeInstalledApp authorizationCodeInstalledApp =
        new AuthorizationCodeInstalledApp(flow, receiver);
    Credential credentials = authorizationCodeInstalledApp.authorize("watho81");

    return credentials;
  }

  public HttpRequestFactory getRequestFactory() throws Exception {
    if (requestFactory == null) {
      init();
    }
    return requestFactory;
  }

  public <T> T executeRequest(GenericUrl fitBUrl, Class<T> dataClazz) throws Exception {
    HttpRequest request = getRequestFactory().buildGetRequest(fitBUrl);
    return request.execute().parseAs(dataClazz);
  }
}
