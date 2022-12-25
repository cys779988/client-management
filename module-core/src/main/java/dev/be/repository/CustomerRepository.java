package dev.be.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.be.domain.model.Customer;
import dev.be.domain.model.ForeignCorporationCustomer;
import dev.be.domain.model.ForeignCustomer;
import dev.be.domain.model.KoreanCorporationCustomer;
import dev.be.domain.model.KoreanCustomer;
import dev.be.domain.model.CustomerBasicInfoResponse;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	Optional<KoreanCustomer> findByNameAndResidentNumber(String name, String residentNumber);

	Optional<KoreanCorporationCustomer> findByNameAndRegistrationNumber(String name, String registrationNumber);

	Optional<ForeignCustomer> findByNameAndBirthDate(String name, LocalDate birthDate);

	Optional<ForeignCorporationCustomer> findByNameAndEstablishmentDate(String name, LocalDate establishmentDate);

	@Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.representive WHERE c.id = :id")
	Optional<Customer> findById(@Param("id") Long id);
	
	List<CustomerBasicInfoResponse> findAllBy();
}
