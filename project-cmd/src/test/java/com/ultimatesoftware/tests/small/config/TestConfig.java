package com.ultimatesoftware.tests.small.config;

import static org.mockito.Mockito.mock;

import com.ultimatesoftware.domain.projectAggregate;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventstore.EventStore;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("small-test")
@Configuration
public class TestConfig {

  @Bean
  public CommandGateway commandGateway() {
    return mock(CommandGateway.class);
  }

  @Bean
  public FixtureConfiguration fixture() {
    return Fixtures.newGivenWhenThenFixture(projectAggregate.class);
  }

  @Bean
  public EventBus eventBus(FixtureConfiguration fixture) {
    return fixture.getEventBus();
  }

  @Bean
  public EventStore getEventStore(FixtureConfiguration fixture) {
    return fixture.getEventStore();
  }

  @Bean
  public CommandBus commandBus(FixtureConfiguration fixture) {
    return fixture.getCommandBus();
  }

  @Bean
  public CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean(CommandBus commandBus) {
    CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<>();
    factory.setCommandBus(commandBus);
    return factory;
  }
}
