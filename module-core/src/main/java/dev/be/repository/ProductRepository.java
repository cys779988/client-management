package dev.be.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.be.domain.model.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	Optional<ProductEntity> findByName(String name);

	@Lock(value = LockModeType.OPTIMISTIC)
	@Query("SELECT p FROM ProductEntity p WHERE p.id = :id")
	Optional<ProductEntity> findByIdWithOptimisticLock(@Param("id") Long productId);

}
