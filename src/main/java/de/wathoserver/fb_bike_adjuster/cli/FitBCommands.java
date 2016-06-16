package de.wathoserver.fb_bike_adjuster.cli;

import java.util.Comparator;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSortedSet;

import de.wathoserver.fb_bike_adjuster.api.FitBApi;
import de.wathoserver.fb_bike_adjuster.api.GetActivitiesAfterUrl;
import de.wathoserver.fb_bike_adjuster.model.Activities;
import de.wathoserver.fb_bike_adjuster.model.Activity;

@Component
public class FitBCommands implements CommandMarker {

  private static final Logger log = LoggerFactory.getLogger(FitBCommands.class);


  @Autowired
  private FitBApi api;

  @CliAvailabilityIndicator({"fb activity list"})
  public boolean isAvailable() {
    // always available
    return true;
  }

  @CliCommand(value = "fb activity list today", help = "Lists all activities of today.")
  public String listActivitiesToday(
  // @CliOption(key = {"message"}, mandatory = true,
  // help = "The hello world message") final String message,
  // @CliOption(key = {"location"}, mandatory = false, help = "Where you are saying hello",
  // specifiedDefaultValue = "At work") final String location
  ) {
    StringBuilder sb = new StringBuilder();
    try {
      Activities activities = api.executeGetRequest(
          new GetActivitiesAfterUrl(FitBApi.LOCAL_DATE_FORMATTER.print(new LocalDateTime())),
          GetActivitiesAfterUrl.DATACLASS);
      ImmutableSortedSet<Activity> sortedActivities =
          ImmutableSortedSet.orderedBy(new Comparator<Activity>() {

            @Override
            public int compare(Activity o1, Activity o2) {
              // TODO Auto-generated method stub
              return 0;
            }
          }).addAll(activities.getActivities()).build();
      int index = 0;
      for (Activity activity : sortedActivities) {
        sb.append("[").append(index).append("] ").append(activity.getActivityName());
        sb.append(OsUtils.LINE_SEPARATOR);
        index++;
      }
    } catch (Exception e) {
      log.error("Could not execute GetRequest", e);
      sb.append("Error executing GetRequest: ").append(e.getLocalizedMessage());
    }
    return sb.toString();
  }

}
