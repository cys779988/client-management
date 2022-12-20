package dev.be.auth.service.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import dev.be.domain.model.UserEntity;


public class UserContext extends User{
	private static final long serialVersionUID = -4248626915860851740L;
	private final UserEntity userEntity;
	
	public UserContext(UserEntity userEntity, Collection<? extends GrantedAuthority> authorities) {
		super(userEntity.getEmail(), userEntity.getPassword(), authorities);
		this.userEntity = userEntity;
	}
	
	public UserEntity getUser() {
		return userEntity;
	}
}
