package dev.be.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {

    @PersistenceContext
    private EntityManager entityManager;

}