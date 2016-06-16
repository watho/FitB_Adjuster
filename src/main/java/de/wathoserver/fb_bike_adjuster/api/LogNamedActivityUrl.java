package de.wathoserver.fb_bike_adjuster.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class LogNamedActivityUrl extends GenericUrl {

  @Key
  private String activityName = "FitBAdjuster-Test";

  @Key
  private long manualCalories = 222;

  @Key
  private String startTime = "16:09:00";

  @Key
  private long durationMillis = Math.round(60000 * 54.5);

  @Key
  private String date = "2016-06-15";

  public LogNamedActivityUrl() {
    super("https://api.fitbit.com/1/user/-/activities.json");
  }
}
