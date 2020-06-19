package com.ultimatesoftware.tests.small.config;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Profile("small-test")
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

  @Override
  public String getDatabaseName() {
    return "project-query";
  }

  @Bean
  @Override
  public Mongo mongo() {
    return fongo().getMongo();
  }

  @Bean
  public Fongo fongo() {
    return new Fongo(getDatabaseName());
  }
}
