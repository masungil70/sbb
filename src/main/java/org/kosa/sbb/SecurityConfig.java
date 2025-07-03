package org.kosa.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*
 * Get  : 4K
 * Post : 제한 없음
 *     form -> input tag -> input file 자료전달  
 * 
 *                              요청 
 * 클라이언트(브라우저) ---------------------> 서버          
 *                      <-------------------(html), 인증키(임의의값, 의미가 있을 수 있음)
 *     form -> input text, file, checkbox, radio,textarea 서버 자료 전달
 *                                                      
 *     .. 사용자 입력 
 *     사용자 전송  ------------------------->   
 *     form -> 인증키, input text, file, checkbox, radio,textarea 서버 자료 전달
 *                                              인증키 확인 후  정상  위에서 받은 자료를 서버에서 처리  
 *                                              인증키 확인 후 비정상 오류발생   
 *     <html>
 *        <head>
 *        </head>
 *        <body>
 *        </body>
 *     </html>                                                                                              
 *                                                   
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChild(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/**").permitAll() // 🔧 잘못된 부분 수정
				)
				.csrf(csrf -> csrf
						.ignoringRequestMatchers(new AntPathRequestMatcher("/imageUpload")) // ⚠ 전체 제외는 주의
				)
				.formLogin(form -> form
						.loginPage("/user/login")
						.defaultSuccessUrl("/")
						.permitAll())
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true));

		return http.build();
	}

	// UserService.passwordEncoder = PasswordEncoder passwordEncoder()이 설정됨
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// @Bean
	// AuthenticationManager authenticationManager(AuthenticationConfiguration
	// authenticationConfiguration) throws Exception{
	// return authenticationConfiguration.getAuthenticationManager();
	// }
}
