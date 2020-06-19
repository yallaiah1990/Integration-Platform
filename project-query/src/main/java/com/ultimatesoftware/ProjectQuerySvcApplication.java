package com.ultimatesoftware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.ultimatesoftware")
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EnableMongoRepositories("com.ultimatesoftware.domain")
public class ProjectQuerySvcApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProjectQuerySvcApplication.class, args);
  }
}
