package dev.be.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.be.domain.model.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	Optional<ProductEntity> findByName(String name);

}
