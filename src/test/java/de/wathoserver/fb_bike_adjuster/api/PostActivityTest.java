package de.wathoserver.fb_bike_adjuster.api;

import org.junit.Test;

import de.wathoserver.fb_bike_adjuster.model.ActivityLog;
import de.wathoserver.fb_bike_adjuster.model.ActivityType;

public class PostActivityTest extends BaseFitBTest {

  @Test
  public void testNamedActivity() throws Exception {
    LogNamedActivityUrl logActivityUrl = new LogNamedActivityUrl();
    ActivityLog result = fitBApi.executePostRequest(logActivityUrl, ActivityLog.class);
    log.debug("result: {}", result);
  }

  @Test
  public void testTypedActivity() throws Exception {
    LogTypedActivityUrl logActivityUrl = new LogTypedActivityUrl(ActivityType.RIDING_IN_A_BUS);
    ActivityLog result = fitBApi.executePostRequest(logActivityUrl, ActivityLog.class);
    log.debug("result: {}", result);
  }
}
