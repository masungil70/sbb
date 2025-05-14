package org.kosa.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSecurityService implements UserDetailsService {

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("step1 username = " + username);
		Optional<SiteUser> _siteUser = userRepository.findByUsername(username);
		log.info("step2 _siteUser = " + _siteUser);
		if (_siteUser.isEmpty()) {
			throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
		}
		log.info("step3 getUsername = " + _siteUser.get().getUsername());
		//권한 저장하는 배열 
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		//실제 로그인한 사용자 정보 
		SiteUser siteUser = _siteUser.get();
		//권한 설정 
		if ("admin".equals(username)) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		} else {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		log.info("step4 authorities = " + authorities);
		
		//스프링 시퀴리티에서 사용하는 사용자 객체로 변환 
		User user = new User(username, siteUser.getPassword(), authorities);
		log.info("step4 user = " + user);
		return user;
	}

}
