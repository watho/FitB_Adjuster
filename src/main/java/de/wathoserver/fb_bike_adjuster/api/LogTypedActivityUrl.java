package de.wathoserver.fb_bike_adjuster.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

import de.wathoserver.fb_bike_adjuster.model.ActivityType;

public class LogTypedActivityUrl extends GenericUrl {

  @Key
  private int activityId;

  @Key
  private String startTime = "16:09:01";

  @Key
  private long durationMillis = Math.round(60000 * 54.5);

  @Key
  private long manualCalories = 222;

  @Key
  private String date = "2016-06-15";

  public LogTypedActivityUrl(ActivityType activityType) {
    super("https://api.fitbit.com/1/user/-/activities.json");
    Preconditions.checkNotNull(activityType);
    this.activityId = activityType.getId();
  }

}
