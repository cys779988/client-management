package dev.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.be.domain.model.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>{

}
