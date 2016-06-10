package de.wathoserver.fb_bike_adjuster.oauth2;

import com.google.api.client.http.GenericUrl;

public class FitBUrl extends GenericUrl {

  public FitBUrl() {
    super("https://api.fitbit.com/1/user/-/profile.json");
  }



}
