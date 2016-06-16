package de.wathoserver.fb_bike_adjuster.oauth2;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.api.client.http.HttpTransport;

import de.wathoserver.fb_bike_adjuster.api.BaseFitBTest;
import de.wathoserver.fb_bike_adjuster.api.GetActivitiesAfterUrl;
import de.wathoserver.fb_bike_adjuster.model.Activities;
import de.wathoserver.fb_bike_adjuster.model.Activity;


public class OAuth2_Test extends BaseFitBTest {

  private static final org.slf4j.Logger log = LoggerFactory.getLogger(OAuth2_Test.class);

  @Test
  public void testOAuth2() throws Exception {
    Logger.getLogger(HttpTransport.class.getName()).setLevel(Level.ALL);
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    getFitBApi().logSettings();

    GetActivitiesAfterUrl getActivitiesAfterDate = new GetActivitiesAfterUrl("2016-06-13");
    // getActivitiesAfterDate.setAfterDate("2016-06-13");

    Activities activities =
        getFitBApi().executeGetRequest(getActivitiesAfterDate, Activities.class);

    assertThat(activities, is(notNullValue()));
    assertThat(activities.getActivities(), is(notNullValue()));
    assertThat(activities.getActivities().size(), is(greaterThan(0)));
    for (Activity activity : activities.getActivities()) {
      log.debug("activity found = {}. Duration={},all={}", activity.getActivityName(),
          activity.getActiveDuration(), activity);
    }
  }
}
