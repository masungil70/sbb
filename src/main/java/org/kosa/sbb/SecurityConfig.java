package org.kosa.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChild(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorizeHttpRequests ->
		                                               // /** -> 루트 경로 이하 모두 허가함 
			authorizeHttpRequests.requestMatchers(new AntPathRequestMatcher("/**")).permitAll());
		
		return http.build();
	}
}
