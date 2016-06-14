package de.wathoserver.fb_bike_adjuster.model;

import java.util.Set;

import com.google.api.client.util.Key;

public class Activities {

  @Key("activities")
  private Set<Activity> activities;

  public Set<Activity> getActivities() {
    return activities;
  }

  public void setActivities(Set<Activity> activities) {
    this.activities = activities;
  }
}
