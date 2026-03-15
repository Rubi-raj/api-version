package com.rubi.apiversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ApiVersionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiVersionApplication.class, args);
	}
}

// ApiVersionController will showcase about api versioning.

@RestController
class ApiVersionController {

	private final ApplicationEventPublisher applicationEventPublisher;

	ApiVersionController(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@GetMapping(value = "/users", version = "1.1")
	public Collection<User> getUsers() {

		applicationEventPublisher.publishEvent(new User(101, "Rubi"));

		return List.of(
				new User(101, "Rubiraj Amutha"),
				new User(102, "Anand")
		);
	}

	@GetMapping(value = "/users", version = "1.0")
	public Collection<Object> getUsersClassic() {
		return List.of(
				new User(101, "Rubiraj Amutha"),
				new User(102, "Anand"),
				Map.of("userid", 103, "username", "Anonymous", "url", "http://localhost:8080/users?api-version=1.0")
		);
	}
}

record User(int userid, String username) {
}

@Component
class UserAudit {
	@EventListener
	void audit(User user) {
		System.out.println("User audit event triggered: " + user);
	}
}

@Component
class UserNotification {
	@EventListener
	void notify(User user) {
		System.out.println("User Notification triggered: " + user);
	}
}
