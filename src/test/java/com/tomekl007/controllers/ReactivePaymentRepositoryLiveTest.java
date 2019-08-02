package com.tomekl007.controllers;

import com.tomekl007.payment.domain.PaymentDto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactivePaymentRepositoryLiveTest {

    @Ignore("it is a live test, so app needs to be started")
    @Test
    public void shouldCreateAndRetrievePayment() {
        //given
        RestTemplate restTemplate = new RestTemplate();
        PaymentDto paymentDto = new PaymentDto(UUID.randomUUID().toString(),
            "123", "456", 200L);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentDto> entity = new HttpEntity<>(paymentDto, httpHeaders);

        //when
        ResponseEntity<PaymentDto> response = restTemplate
                .postForEntity("http://localhost:8080/reactive/payment",
                    entity, PaymentDto.class);

        //then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //when
        List<PaymentDto> getResponse = restTemplate.exchange(
            "http://localhost:8080/reactive/payment/" +
                response.getBody().getUserId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PaymentDto>>() {
                }).getBody();

        //then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(getResponse.size()).isGreaterThan(0);

    }


    public static void main(String [] args) {
        // primitive type)
        Integer arr[] = { 5, 6, 7, 8, 1, 2, 3, 4, 3 };

        List<Integer> strs = new ArrayList<Integer>() ;
        strs.addAll(Arrays.asList(arr)) ;
        System.out.println(strs);

        // Set demonstration using HashSet Constructor
        Set<Integer> set = new HashSet<Integer>(Arrays.asList(arr));
        // will not cause errors/exceptions.
        set.add(new Integer(3)) ;

        // Duplicate elements are not printed in a set.
        System.out.println(set);
    }


}