package de.wathoserver.fb_bike_adjuster.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class FitBGetActivitiesUrl extends GenericUrl {

  @Key
  private String afterDate;

  @Key
  private String offset = "0";

  @Key
  private String limit = "20";

  @Key
  private String sort = "asc";

  public FitBGetActivitiesUrl() {
    super("https://api.fitbit.com/1/user/-/activities/list.json");
  }

  public String getAfterDate() {
    return afterDate;
  }

  public void setAfterDate(String afterDate) {
    this.afterDate = afterDate;
  }
}
