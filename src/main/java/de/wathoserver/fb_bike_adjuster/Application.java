package de.wathoserver.fb_bike_adjuster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@SpringBootApplication
@PropertySource("classpath:/app.properties")
public class Application implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	/*
	 * PropertySourcesPlaceHolderConfigurer Bean only required for @Value("{}")
	 * annotations. Remove this bean if you are not using @Value annotations for
	 * injecting properties.
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Value("${debug:false}")
	private boolean debug;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public boolean isDebug() {
		return debug;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.debug("started with debug={}", debug);
	}

}
