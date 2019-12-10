package com.eb.admin;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.eb.admin.controller.CommonFilter;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public FilterRegistrationBean encodefilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration
				.setFilter(new org.springframework.web.filter.CharacterEncodingFilter());
		registration.addUrlPatterns("/*");
		registration.addInitParameter("encoding", "UTF-8");
		registration.addInitParameter("forceEncoding", "true");
		registration.setName("encoderFilter");
		return registration;
	}

	@Bean
	public FilterRegistrationBean filterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(commonFilter());
		registration.addUrlPatterns("/*");
		registration.setName("commonFilter");
		return registration;
	}

	@Bean(name = "commonFilter")
	public Filter commonFilter() {
		return new CommonFilter();
	}
}