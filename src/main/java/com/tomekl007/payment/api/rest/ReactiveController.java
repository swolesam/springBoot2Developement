package com.tomekl007.payment.api.rest;

import com.tomekl007.payment.domain.PaymentDto;
import com.tomekl007.payment.infrastructure.persistance.ReactivePaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.metrics.instrument.DistributionSummary;
import org.springframework.metrics.instrument.MeterRegistry;
import org.springframework.metrics.instrument.Timer;
import org.springframework.metrics.instrument.stats.hist.NormalHistogram;
import org.springframework.metrics.instrument.stats.quantile.GKQuantiles;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController()
public class ReactiveController {

  private final ReactivePaymentRepository paymentRepository;
  private final DistributionSummary distributionSummary;
  private final Timer postTimer;

  @Autowired
  public ReactiveController(ReactivePaymentRepository paymentRepository,  MeterRegistry meterRegistry) {
    this.paymentRepository = paymentRepository;
    postTimer = meterRegistry
        .timerBuilder("payment_create")
        .quantiles(GKQuantiles.quantiles(0.99, 0.90, 0.80, 0.50).create()).create();

    distributionSummary = meterRegistry.summaryBuilder("user_id_length")
        .histogram(
            new NormalHistogram(NormalHistogram.linear(0, 5, 10))
        )
        .create();
  }

  @GetMapping("/reactive/payment/{userId}")
  public Mono<List<PaymentDto>> getPayments(@PathVariable final String userId) {
    distributionSummary.record(userId.length());
    return paymentRepository.getPayments(userId);
  }

  @PostMapping(value = "/reactive/payment", consumes = MediaType.APPLICATION_JSON)
  public PaymentDto addPayment(@RequestBody PaymentDto payment) {
    return postTimer.record(() -> paymentRepository.addPayments(payment).block());

  }

}
