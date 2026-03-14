package com.rubi.apiversion;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@EnableResilientMethods
@Configuration
public class ResilientInSpring {

	@Bean
	ApplicationRunner runner(RiskyClient riskyClient) {
		return args -> {
			System.out.println("Simulating Spring Resiliency");
			riskyClient.doSomethingThatMightFail();
		};
	}
}

// These Resilient Features introduced in Spring 7.0

@Component
class RiskyClient {

	private AtomicInteger counter = new AtomicInteger(0);

	@ConcurrencyLimit(limit = 10)
	@Retryable(maxRetries = 4, delay = 3000, includes = RuntimeException.class)
	void doSomethingThatMightFail() throws Exception {

		if (this.counter.incrementAndGet() <= 3) {
			System.out.println("Something went wrong, retrying " + counter.get());
			throw new RuntimeException("Something went wrong, retrying " + counter.get());
		}
		System.out.println("Success");
	}

}
