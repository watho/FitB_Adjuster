package de.wathoserver.fb_bike_adjuster.cli;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.shell.support.logging.HandlerUtils;

/**
 * spring shell application
 *
 * @author linux_china
 * @see https://github.com/linux-china/spring-boot-starter-shell
 */
public class SpringShellApplication {

  public static void run(Object source, String... args) {
    run(new Object[] {source}, args);
  }

  public static void run(Object[] sources, String[] args) {
    ConfigurableApplicationContext ctx = new SpringApplication(sources).run(args);
    try {
      new BootShim(args, ctx).run();
    } finally {
      HandlerUtils.flushAllHandlers(Logger.getLogger(""));
    }
  }

}
