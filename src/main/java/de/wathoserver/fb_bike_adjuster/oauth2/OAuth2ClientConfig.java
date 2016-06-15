package de.wathoserver.fb_bike_adjuster.oauth2;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2ClientConfig {

  /** Value of the "API Key". */
  @Value("${apiKey}")
  @NotNull
  private String apiKey;

  /** Value of the "API Secret". */
  @Value("${apiSecret}")
  @NotNull
  private String apiSecret;

  /** Port in the "Callback URL". */
  @Value("${port:8080}")
  private int port;

  /** Domain name in the "Callback URL". */
  @Value("${domain:127.0.0.1}")
  private String domain;

  public String getApiKey() {
    return apiKey;
  }

  public String getApiSecret() {
    return apiSecret;
  }

  public int getPort() {
    return port;
  }

  public String getDomain() {
    return domain;
  }

}
