package com.tomekl007.payment.infrastructure.filter;

import com.tomekl007.payment.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class TransactionFilter implements Filter {
	private final static Logger LOG = LoggerFactory.getLogger(TransactionFilter.class);

    @Autowired
    private TransactionService transactionService;


	@Override
	public void init(FilterConfig filterConfig) {
		LOG.info("Initializing filter :{}", this);

		/**
		 * [selnasr] Adding Autowiring in Java Filter.. forces javax class to support Spring Beans so the
		 * autowired spring bean (TransactionService) above does not return null pointer, it will return
		 * the actual spring bean
		 **/
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		transactionService.startTransaction(req) ;
		try {
			chain.doFilter(request, response);
			transactionService.commitTransaction(req);
		} catch(Exception e) {
			transactionService.rollbackTransaction(req);
		}

	}

	@Override
	public void destroy() {
	    LOG.warn("Destructing filter :{}", this);
	}
}
