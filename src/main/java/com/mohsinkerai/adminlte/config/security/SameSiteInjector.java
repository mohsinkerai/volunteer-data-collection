package com.mohsinkerai.adminlte.config.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class SameSiteInjector {

  private final ApplicationContext applicationContext;

  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {
    DefaultCookieSerializer cookieSerializer = applicationContext.getBean(DefaultCookieSerializer.class);
    log.info("Received DefaultCookieSerializer, Overriding SameSite Strict");
    cookieSerializer.setSameSite("strict");
  }
}
