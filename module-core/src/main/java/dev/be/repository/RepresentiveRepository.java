package dev.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.be.domain.model.RepresentiveEntity;

public interface RepresentiveRepository extends JpaRepository<RepresentiveEntity, Long>{

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM RepresentiveEntity r WHERE r.customer.id = :customerId")
	void deleteAllByCustomerId(@Param("customerId") Long customerId);

}
