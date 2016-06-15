package de.wathoserver.fb_bike_adjuster.oauth2;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.api.client.http.HttpTransport;

import de.wathoserver.fb_bike_adjuster.Application;
import de.wathoserver.fb_bike_adjuster.model.Activities;
import de.wathoserver.fb_bike_adjuster.model.Activity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class OAuth2_Test {

  private static final org.slf4j.Logger log = LoggerFactory.getLogger(OAuth2_Test.class);

  @Autowired
  private FitBApi fitBApi;

  @Test
  public void testOAuth2() throws Exception {
    Logger.getLogger(HttpTransport.class.getName()).setLevel(Level.ALL);
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    fitBApi.logSettings();

    FitBGetActivitiesUrl getActivitiesAfterDate = new FitBGetActivitiesUrl();
    getActivitiesAfterDate.setAfterDate("2016-06-13");

    Activities activities = fitBApi.executeRequest(getActivitiesAfterDate, Activities.class);

    assertThat(activities, is(notNullValue()));
    assertThat(activities.getActivities(), is(notNullValue()));
    assertThat(activities.getActivities().size(), is(greaterThan(0)));
    for (Activity activity : activities.getActivities()) {
      log.debug("activity found = {}. Duration={},all={}", activity.getActivityName(),
          activity.getActiveDuration(), activity);
    }
  }


}
