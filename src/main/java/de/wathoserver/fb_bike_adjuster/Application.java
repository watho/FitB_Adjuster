package de.wathoserver.fb_bike_adjuster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import de.wathoserver.fb_bike_adjuster.oauth2.FitBApi;

@SpringBootApplication
@EnableConfigurationProperties()
public class Application implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  @Autowired
  private FitBApi fitBApi;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    fitBApi.logSettings();
  }

}
