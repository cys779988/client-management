package dev.be.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import dev.be.auth.service.dto.JoinRequest;
import dev.be.auth.service.dto.UserContext;
import dev.be.domain.model.Role;
import dev.be.domain.model.UserEntity;
import dev.be.exception.BusinessException;
import dev.be.exception.ErrorCode;
import dev.be.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
	private final UserRepository userRepository;
	@Transactional
	public String signUp(JoinRequest request) {
		isDuplicatedEmail(request.getEmail());
		
		isDuplicatedName(request.getName());
		
		request.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
		UserEntity entity = request.toEntity(Role.ADMIN);
		return userRepository.save(entity).getEmail();
	}
	
	private void isDuplicatedEmail(String email) {
		Optional<UserEntity> userEntityWrapper = userRepository.findById(email);

		if (userEntityWrapper.isPresent()) {
			throw new BusinessException(ErrorCode.DUPLICATED_EMAIL);
		}
	}
	
	private void isDuplicatedName(String name) {
		Optional<UserEntity> userEntityWrapper = userRepository.findByNameContaining(name);
		
		if (userEntityWrapper.isPresent()) {
			throw new BusinessException(ErrorCode.DUPLICATED_NAME);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		if(!StringUtils.hasText(userEmail)) {
			throw new UsernameNotFoundException(ErrorCode.BAD_LOGIN_EAMIL.getMessage());
		}
		
		Optional<UserEntity> userEntityWrapper = userRepository.findById(userEmail);

		if (!userEntityWrapper.isPresent()) {
			throw new UsernameNotFoundException(ErrorCode.BAD_LOGIN_EAMIL.getMessage());
		}
		
		UserEntity userEntity = userEntityWrapper.get();

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));

		UserContext userContext = new UserContext(userEntity, authorities);

		return userContext;
	}
}
