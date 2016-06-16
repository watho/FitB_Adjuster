package de.wathoserver.fb_bike_adjuster.api;

import org.junit.Test;

import de.wathoserver.fb_bike_adjuster.model.Calories;

public class GetCaloriesTest extends BaseFitBTest {
  @Test
  public void testGetCalories() throws Exception {
    GetCaloriesUrl caloriesUrl = new GetCaloriesUrl();
    Calories cals = fitBApi.executeGetRequest(caloriesUrl, Calories.class);
    log.debug("cals: {}", cals);
  }
}
