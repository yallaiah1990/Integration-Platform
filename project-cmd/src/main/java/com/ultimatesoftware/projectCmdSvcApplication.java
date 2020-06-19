package com.ultimatesoftware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "com.ultimatesoftware")
@EnableDiscoveryClient
@EnableAspectJAutoProxy
public class projectCmdSvcApplication {

  public static void main(String[] args) {
    SpringApplication.run(projectCmdSvcApplication.class, args);
  }
}
