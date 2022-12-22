package dev.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.be.domain.model.SalesHistoryEntity;

public interface SalesHistoryRepository extends JpaRepository<SalesHistoryEntity, Long>{

}
