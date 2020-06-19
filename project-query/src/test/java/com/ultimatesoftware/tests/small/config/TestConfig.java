package com.ultimatesoftware.tests.small.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Profile("small-test")
@Configuration
@ComponentScan("com.ultimatesoftware")
@EnableMongoRepositories({"com.ultimatesoftware.domain"})
public class TestConfig {

  @Bean
  public EventBus eventBus() {
    return new SimpleEventBus();
  }

  @Bean
  public CommandBus commandBus() {
    return new SimpleCommandBus();
  }
}
