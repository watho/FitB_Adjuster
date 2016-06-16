package de.wathoserver.fb_bike_adjuster.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Return class of LogActivityUrl.
 *
 * @author watho
 *
 */
public class ActivityLog extends GenericJson {

  @Key
  private String activityId;

  public String getActivityId() {
    return activityId;
  }
}
