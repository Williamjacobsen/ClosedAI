package com.closedai.closedai.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
  @Bean
  public FilterRegistrationBean<StatelessSessionFilter> sessionFilter(
          StatelessSessionFilter f) {
      FilterRegistrationBean<StatelessSessionFilter> reg = new FilterRegistrationBean<>(f);
      reg.setOrder(0);              // run first
      return reg;
  }
}
