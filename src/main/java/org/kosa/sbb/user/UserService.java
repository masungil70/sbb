package org.kosa.sbb.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/*
 * 암호화 : 대칭키 암호화    -> 복구 가능 
 *          비대칭키 암호화  -> 복구가 안됨 
 */


@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public SiteUser create(String username, String email, String password) {
		//회원 정보 객체 생성 
		SiteUser user = SiteUser.of(username, passwordEncoder.encode(password), email);
		
		//저장소에 회원 정보 등록 
		userRepository.save(user);
		
		return user;
	}
	
}
