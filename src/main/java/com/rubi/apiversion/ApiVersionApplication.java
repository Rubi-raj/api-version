package com.rubi.apiversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

@RestController
class ApiVersionController {

	@GetMapping(value = "/users", version = "1.1")
	public Collection<User> getUsers() {
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
