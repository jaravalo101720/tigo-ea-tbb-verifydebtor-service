package com.tigo.ea.tbb.verifydebtor.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.htc.ea.mcs.consumer.config.ServiceConsumerRestConfig;
import com.htc.ea.util.configuration.UtilAppConfig;
import com.tigo.ea.tbb.verifydebtor.util.HttpServletInterceptor;


@Configuration
@Import({UtilAppConfig.class, ServiceConsumerRestConfig.class})
public class AppConfig {
		
	/*
	 * Bean interceptor de ip cliente
	 */
	@Bean
	public FilterRegistrationBean httpInterceptorFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new HttpServletInterceptor());
		return registration;
	}

}