package de.wathoserver.fb_bike_adjuster.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

import de.wathoserver.fb_bike_adjuster.model.Activities;

public class GetActivitiesAfterUrl extends GenericUrl {

  public static final Class<Activities> DATACLASS = Activities.class;

  @Key
  private String afterDate;

  @Key
  private String offset = "0";

  @Key
  private String limit = "20";

  @Key
  private String sort = "asc";

  public GetActivitiesAfterUrl(String afterDate) {
    super("https://api.fitbit.com/1/user/-/activities/list.json");
    this.afterDate = afterDate;
  }

  public String getAfterDate() {
    return afterDate;
  }

  public void setAfterDate(String afterDate) {
    this.afterDate = afterDate;
  }

}
