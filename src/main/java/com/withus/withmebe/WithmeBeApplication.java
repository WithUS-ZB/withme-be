package com.withus.withmebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WithmeBeApplication {

  public static void main(String[] args) {
    SpringApplication.run(WithmeBeApplication.class, args);
  }

}
