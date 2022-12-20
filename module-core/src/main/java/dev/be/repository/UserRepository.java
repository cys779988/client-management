package dev.be.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.be.domain.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	
	Optional<UserEntity> findByNameContaining(String keyword);
}