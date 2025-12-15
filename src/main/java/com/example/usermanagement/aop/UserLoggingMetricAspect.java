package com.example.usermanagement.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class UserLoggingMetricAspect {
	
	private final MeterRegistry meterRegistry;
	
	@Around("execution(* com.example.usermanagement.service.UserService.*(..))")
	public Object logUserService(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
	    String methodName = signature.getName();

	    log.info("Calling UserService method: {}", methodName);

	    Timer.Sample sample = Timer.start(meterRegistry);
	    
	    Counter errorCounter = Counter.builder("user_service_method_errors_total")
                .description("Number of exceptions in UserService methods")
                .tag("method", methodName)
                .register(meterRegistry);

	    try {
	        Object result = pjp.proceed();
	        log.info("UserService method {} completed successfully", methodName);
	        return result;
	    } catch (Throwable ex) {
	        log.warn("Exception in UserService method {}: {}", methodName, ex.getMessage());
	        errorCounter.increment();
	        throw ex;
	    } finally {
	        sample.stop(
	            Timer.builder("user_service_method_execution_seconds")
	                 .description("Execution time of UserService methods")
	                 .tag("method", methodName)
	                 .register(meterRegistry)
	        );
	    }
	}

}
