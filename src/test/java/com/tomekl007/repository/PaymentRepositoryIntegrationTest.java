package com.tomekl007.repository;

import com.tomekl007.payment.domain.Payment;
import com.tomekl007.payment.infrastructure.persistance.PaymentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentRepositoryIntegrationTest {
    private final static Logger LOG = LoggerFactory.getLogger(PaymentRepositoryIntegrationTest.class);

    @Autowired
    PaymentRepository paymentRepository;

    @Test
    public void shouldSavePaymentAndRetrieveItByUserId() {
        //given (cucumber style)
        String userId = "joseph";
        // [selnasr] ID (PK) is not set explicitly, it will be populated by spring data according to the AutoGenerate scheme declared (AUTO vs SEQUENCE...)
        Payment payment = new Payment(userId, "124215", "t5356315", 23L);
        LOG.info("Payment Object Pre Save op: {}" , payment);

        //when (cucumber style)
        paymentRepository.save(payment);
        LOG.info("Payment Object Post Save op: {}" , payment);
        List<Payment> payments = paymentRepository.findByUserId(userId);

        //then (cucumber style)
        assertThat(payments.get(0).getAccountFrom()).isEqualTo("124215");
        assertThat(payments.get(0).getAccountTo()).isEqualTo("t5356315");
    }

    @Test
    public void shouldRetrieveAllPaymentsThatHave123AsAccountFrom() {
        //given
        List<Payment> payments = Arrays.asList(
                new Payment(UUID.randomUUID().toString(), "123", "55555", 23L),
                new Payment(UUID.randomUUID().toString(), "123", "77777", 23L),
                new Payment(UUID.randomUUID().toString(), "77777", "2145", 23L)
        );

        //when
        paymentRepository.saveAll(payments);
        List<Payment> foundPayments = paymentRepository.findByFromAccount("123");

        //then
        assertThat(foundPayments.size()).isEqualTo(2);
    }

}