package hr.rba.credit_card_client_service.configuration.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

  @Around(
      "(@annotation(org.springframework.web.bind.annotation.GetMapping) || "
          + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
          + "@annotation(org.springframework.web.bind.annotation.PatchMapping) || "
          + "@annotation(org.springframework.web.bind.annotation.DeleteMapping)) "
          + "&& @annotation(hr.rba.credit_card_client_service.configuration.logging.LogExecutionTime)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();

    log.info("Starting execution method {}", methodName);

    Object result = joinPoint.proceed();

    log.info("Finished execution method {}", methodName);

    return result;
  }
}
