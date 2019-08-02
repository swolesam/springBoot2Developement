package com.tomekl007;

import com.tomekl007.payment.api.PaymentService;
import io.prometheus.client.CollectorRegistry;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.metrics.export.prometheus.EnablePrometheusMetrics;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableHystrix
/**
 * trace back to the parents of the @EnablePrometheusMetrics annotation, you will find chains & chains
 * of @Configuration beans for metrics, monitoring,...etc injected for you by simply importing the mother
 * Prometheus annotation here =  @EnablePrometheusMetrics
 **/
@EnablePrometheusMetrics
/**
 * You can use @ComponentScan("com.howtodoinjava.web") here
 */
public class PaymentApplication {
    // [SELNASR] PaymentService below is an Interface. If there is multiple implementing classes (Spring Components) in the project, Spring will
    // not know which one to bind, hence will throw exception.
    @Autowired(required = false)
    PaymentService paymentService;

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }


    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
        result.setMaxTotal(20);
        return result;
    }

    @Bean
    public RequestConfig requestConfig() {
        RequestConfig result = RequestConfig.custom()
            .setConnectionRequestTimeout(1000)
            .setConnectTimeout(500)
            .setSocketTimeout(500)
            .build();
        return result;
    }
    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
        CloseableHttpClient result = HttpClientBuilder
            .create()
            .setConnectionManager(poolingHttpClientConnectionManager)
            .setDefaultRequestConfig(requestConfig)
            .build();
        return result;
    }

    @Bean
    public RestTemplate restTemplate(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }

    static {
        //HACK Avoids duplicate metrics registration in case of Spring Boot dev-tools restarts
        CollectorRegistry.defaultRegistry.clear();
    }

}
