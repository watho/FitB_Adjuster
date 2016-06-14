package de.wathoserver.fb_bike_adjuster.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Activity extends GenericJson {

  @Key
  private String activityName;

  @Key
  private long activeDuration;

  public String getActivityName() {
    return activityName;
  }

  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }

  public long getActiveDuration() {
    return activeDuration;
  }

  public void setActiveDuration(long activeDuration) {
    this.activeDuration = activeDuration;
  }
}
