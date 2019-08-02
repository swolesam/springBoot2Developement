package com.tomekl007.payment.infrastructure.configuration;

import com.netflix.discovery.converters.Auto;
import com.tomekl007.payment.infrastructure.filter.InterceptRequestResponseFilter;
import com.tomekl007.payment.infrastructure.filter.TransactionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Notice this is declared as @Configuration
 */
@Configuration
public class FilterConfig {


    /** See more examples @ https://www.concretepage.com/spring-boot/spring-boot-filter  **/
    /**
     * [selnasr] register  InterceptRequestResponseFilter Filter here , returns FilterRegistrationBean<InterceptRequestResponseFilter>
     *     which will dictate to spring how to handle the Filter.
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<InterceptRequestResponseFilter> loggingFilter() {
        FilterRegistrationBean<InterceptRequestResponseFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new InterceptRequestResponseFilter());

        // tell Spring what URL Patterns to execute this filter on
        registrationBean.addUrlPatterns("/users/*");
        registrationBean.setOrder(1);

        // tell spring the order to execute this filter
        // registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE -1);

        return registrationBean;

    }

    @Bean
    public FilterRegistrationBean<TransactionFilter> transactionFilter() {
        FilterRegistrationBean<TransactionFilter> txFilter = new FilterRegistrationBean<>() ;
        txFilter.setFilter(new TransactionFilter());

        txFilter.addUrlPatterns("/users/*");
        txFilter.setOrder(2); // run this filter AFTER running Logging Filter

        return txFilter ;
    }

    /**
     * ....
     * ...
     * ..
     *
     * [selnasr] register more filters as needed, 1 method per filter
     */
}
