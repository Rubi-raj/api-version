package com.rubi.apiversion;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.registry.ImportHttpServices;

import java.util.Collection;

// This HttpServices is an example without much implementation spring boot autoconfigure HTTPClients.

@ImportHttpServices(CatFacts.class)
@Configuration
public class HttpServices {

	@Bean
	ApplicationRunner applicationRunner(CatFacts catFacts) {
		return args -> {
			System.out.println("Executing CatFacts..!");
			catFacts.getCatFacts().facts().forEach(System.out::println);

			System.out.println("Executing PlainCatFacts..!");
			System.out.println(catFacts.getPlainCatFacts());

		};
	}
}

record CatFact(@JsonProperty("fact_number") int factNumber, String fact) {
}

record CatFactsResponse(Collection<CatFact> facts) {
}

@Component
interface CatFacts {

	@GetExchange(value = "https://www.catfacts.net/api/")
	CatFactsResponse getCatFacts(); // Structured DTO

	@GetExchange(url = "https://www.catfacts.net/api/")
	Object getPlainCatFacts(); // Plain String
}
