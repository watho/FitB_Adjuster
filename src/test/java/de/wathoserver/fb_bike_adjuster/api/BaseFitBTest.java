package de.wathoserver.fb_bike_adjuster.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.wathoserver.fb_bike_adjuster.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class BaseFitBTest {

  protected static final Logger log = LoggerFactory.getLogger(BaseFitBTest.class);


  @Autowired
  protected FitBApi fitBApi;

  @Before
  public void beforeClass() {
    assertThat(fitBApi, is(notNullValue()));
  }

  public FitBApi getFitBApi() {
    return fitBApi;
  }
}
