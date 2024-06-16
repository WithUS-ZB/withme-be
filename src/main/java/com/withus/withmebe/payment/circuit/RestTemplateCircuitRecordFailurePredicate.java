package com.withus.withmebe.payment.circuit;

import java.util.function.Predicate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class RestTemplateCircuitRecordFailurePredicate implements Predicate<Throwable> {


  @Override
  public boolean test(Throwable throwable) {
    return !(throwable instanceof HttpClientErrorException);
  }
}
