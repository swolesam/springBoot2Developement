package com.tomekl007.measurments;


import org.junit.Test;
import org.springframework.metrics.instrument.MeterRegistry;
import org.springframework.metrics.instrument.Timer;
import org.springframework.metrics.instrument.simple.SimpleMeterRegistry;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseTImeMetricsTest {

  private MeterRegistry metricRegistry = new SimpleMeterRegistry();

  @Test
  public void shouldMeasureTimeOfMethod(){
    //given
    Timer timerOfMethod = metricRegistry.timer("timerOfMethod");

    //when
    timerOfMethod.record(this::expensiveMethodCall);

    //then
    double v = timerOfMethod.totalTime(TimeUnit.MILLISECONDS);
    System.out.println("Total Time of Execution for method = " + v/1000 + " seconds") ;
    assertThat(v).isGreaterThanOrEqualTo(1000);

  }

  public void expensiveMethodCall(){
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
