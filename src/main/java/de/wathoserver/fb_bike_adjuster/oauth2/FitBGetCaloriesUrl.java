package de.wathoserver.fb_bike_adjuster.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class FitBGetCaloriesUrl extends GenericUrl {

  @Key
  private String afterDate;

  @Key
  private String offset = "0";

  @Key
  private String limit = "20";

  @Key
  private String sort = "asc";

  public FitBGetCaloriesUrl() {
    super("https://api.fitbit.com/1/user/-/activities/calories/date/2016-06-14/2016-06-14/1min/time/17:11/17:16.json");
  }

  public String getAfterDate() {
    return afterDate;
  }

  public void setAfterDate(String afterDate) {
    this.afterDate = afterDate;
  }
}
