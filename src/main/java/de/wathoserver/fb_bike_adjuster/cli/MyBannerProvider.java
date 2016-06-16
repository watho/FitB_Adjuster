package de.wathoserver.fb_bike_adjuster.cli;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyBannerProvider extends DefaultBannerProvider {

  @Override
  public String getBanner() {
    StringBuffer buf = new StringBuffer();
    buf.append("=======================================" + OsUtils.LINE_SEPARATOR);
    buf.append("*                                     *" + OsUtils.LINE_SEPARATOR);
    buf.append("*            FitB_Bike_Adjuster       *" + OsUtils.LINE_SEPARATOR);
    buf.append("*                                     *" + OsUtils.LINE_SEPARATOR);
    buf.append("=======================================" + OsUtils.LINE_SEPARATOR);
    buf.append("Version:" + this.getVersion());
    return buf.toString();
  }

  @Override
  public String getVersion() {
    return "0.0.1-SNAPSHOT";
  }

  @Override
  public String getWelcomeMessage() {
    return "Welcome to FitB_Bike_Adjuster CLI";
  }

  @Override
  public String getProviderName() {
    return "Banner";
  }
}
