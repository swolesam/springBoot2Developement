package com.tomekl007.payment.infrastructure.persistance;


import com.tomekl007.payment.domain.Payment;
import com.tomekl007.payment.domain.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReactivePaymentRepository {

    // notice the use of "final" keyword here since its set via Constructor-based injection
    private final PaymentRepository paymentRepository;

    /**
     * Constructor-based Spring IoC Container Injection
     * Its important here that PaymentRepository (The param) is a Spring Component
     * @param paymentRepository
     */
    @Autowired
    public ReactivePaymentRepository(PaymentRepository paymentRepository) {

        this.paymentRepository = paymentRepository;
    }

    /**
     * [selnasr] This method is implemented async so it will run sometime in the future, and the method returns IMMIDIATELY
     *
     * @param userId
     * @return
     */
    public Mono<List<PaymentDto>> getPayments(String userId) {
        return Mono.defer(() -> Mono.just(paymentRepository.findByUserId(userId)))
                .subscribeOn(Schedulers.elastic())
                .map(p ->
                        p.stream().map(p1 -> new PaymentDto(p1.getUserId(), p1.getAccountFrom(), p1.getAccountTo(), p1.getAmount()))
                                .collect(Collectors.toList()));
    }

    /**
     * [selnasr] This method is implemented async so it will run sometime in the future, and the method returns IMMIDIATELY
     *
     * @param payment
     * @return
     */
    public Mono<PaymentDto> addPayments(final PaymentDto payment) {
        return Mono.just(payment)
                .map(t -> new Payment(t.getUserId(), t.getAccountFrom(), t.getAccountTo(), t.getAmount()))
                .publishOn(Schedulers.parallel())
                .doOnNext(paymentRepository::save)
                .map(t -> new PaymentDto(t.getUserId(), t.getAccountFrom(), t.getAccountTo(), t.getAmount()));
    }
}
