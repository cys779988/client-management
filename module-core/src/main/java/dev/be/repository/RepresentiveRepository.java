package dev.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.be.domain.model.RepresentiveEntity;

public interface RepresentiveRepository extends JpaRepository<RepresentiveEntity, Long>{

}
