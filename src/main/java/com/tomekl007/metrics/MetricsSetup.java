package com.tomekl007.metrics;

import io.prometheus.client.CollectorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.metrics.instrument.Clock;
import org.springframework.metrics.instrument.MeterRegistry;
import org.springframework.metrics.instrument.prometheus.PrometheusMeterRegistry;

/**
 * SELNASR - Spring will scan files with Configuration Annotation 1st before anything else.
 * Once its scanned, Spring IOC Container will init & create these beans declared in here and have them
 * as dependencies available for injection via autowire for example.
 */
@Configuration
public class MetricsSetup {
  @Bean
  public Clock micrometerClock() {
    return Clock.SYSTEM;
  }

  /**  [SELNASR] You can comment this out IF you enable PaymentApplication.java annotation @EnablePrometheusMetrics
   *
  @Bean
  public MeterRegistry prometheusMeterRegistry(CollectorRegistry collectorRegistry,
                                               Clock clock) {
    return new PrometheusMeterRegistry(collectorRegistry, clock);
  }
**/

  @Bean
  public CollectorRegistry collectorRegistry() {
    return new CollectorRegistry(true);
  }

}
