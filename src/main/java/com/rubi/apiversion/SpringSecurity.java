package com.rubi.apiversion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authorization.EnableMultiFactorAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@EnableMultiFactorAuthentication(authorities = {
		FactorGrantedAuthority.PASSWORD_AUTHORITY,
		FactorGrantedAuthority.OTT_AUTHORITY
})
@Configuration
public class SpringSecurity {

	@Bean
	Customizer<HttpSecurity> httpSecurityCustomizer() throws Exception {
		return httpSecurity -> httpSecurity
				.webAuthn(w -> w
						.rpId("localhost")
						.rpName("spring Bootifull")
						.allowedOrigins("http://localhost:8080")
				)
				.oneTimeTokenLogin(o -> o.tokenGenerationSuccessHandler(
						(request, response, oneTimeToken) -> {
							response.getWriter().println("You got a console mail..!");
							response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);

							System.out.println("Please go to http://localhost:8080/login/ott?token=" + oneTimeToken.getTokenValue());

						}
				));
	}

	// Note:- For pass key goto this URL to register passkey first http://localhost:8080/webauthn/register

	@Bean
	InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		return new InMemoryUserDetailsManager(User.withUsername("rubi").password(passwordEncoder().encode("pw")).roles("USER").build());
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	// This will migrate the user password encryption to latest encryption Eg:- Bcrypt current latest.
	@Bean
	UserDetailsPasswordService userDetailsPasswordService(JdbcUserDetailsManager manager) {
		return (user, newPassword) -> {
			UserDetails userDetails = User.withUserDetails(user).password(newPassword).build();
			manager.updateUser(userDetails);
			return userDetails;
		};
	}

	@Bean
	JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}

}
