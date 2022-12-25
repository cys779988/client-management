package dev.be.customer;
import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import dev.be.domain.model.Customer;
import dev.be.domain.model.CustomerType;
import dev.be.domain.model.ForeignCorporationCustomer;
import dev.be.domain.model.KoreanCorporationCustomer;
import dev.be.domain.model.KoreanCustomer;
import dev.be.domain.model.RepresentiveEntity;
import dev.be.repository.CustomerRepository;
import dev.be.repository.RepresentiveRepository;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CustomerDataJpaTest {

	@Autowired
	EntityManager em;
	
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	RepresentiveRepository representiveRepository;
	
	private Customer getForeignCorporationCustomer() {
		return ForeignCorporationCustomer.builder()
					.name("이순신")
					.englishName("lee")
					.email("lee@naver.com")
					.address("테스트주소")
					.contact("010-0000-1111")
					.nationality("미국")
					.registDate(LocalDate.of(1999, 12, 30))
					.build();
					
	}
	
	@Test
	@DisplayName("고객 정보 저장")
	public void saveCustomer() {
		Customer customer = new KoreanCustomer(null, "홍길동", "hone@naver.com", "테스트주소", "010-1111-1111", "99999-111111");
		
		Long id = customerRepository.saveAndFlush(customer).getId();
		
		em.clear();
		
		customer = customerRepository.findById(id).get();
		
		assertThat(customer.getType().equals(CustomerType.KOREAN), is(true));
	}
	
	@Test
	@DisplayName("고객 정보 저장")
	public void saveCorporationCustomer() {
		Customer customer = new KoreanCorporationCustomer(null, "홍길동", "hone@naver.com", "테스트주소", "010-1111-1111", "99999-111111");
		
		representiveRepository.saveAll(Arrays.asList(new RepresentiveEntity(null, customer, "테스트", "010-111-1533")));
		Long id = customerRepository.saveAndFlush(customer).getId();
		em.clear();
		
		customer = customerRepository.findById(id).get();
		
		assertThat(customer.getRepresentive().isEmpty(), is(false));
	}
	
	@Test
	@DisplayName("고객 조회")
	public void findCorporationCustomer() {
		Customer customer = getForeignCorporationCustomer();
		
		representiveRepository.saveAll(Arrays.asList(new RepresentiveEntity(null, customer, "테스트", "010-111-1533")));
		Long id = customerRepository.saveAndFlush(customer).getId();
		
		em.clear();
		
		customer = customerRepository.findById(id).get();
		
		assertThat(customer.getRepresentive().isEmpty(), is(false));
	}
	
	@Test
	@DisplayName("고객 정보 타입 수정")
	public void modifyCustomerType() {
		Customer testCustomer = getForeignCorporationCustomer();
		
		representiveRepository.saveAll(Arrays.asList(new RepresentiveEntity(null, testCustomer, "테스트", "010-111-1533")));
		Long id = customerRepository.saveAndFlush(testCustomer).getId();
		
		em.clear();
		
		customerRepository.deleteById(id);
		
		Customer modifyCustomer = KoreanCustomer.builder()
				.id(id)
				.name("권율")
				.email("k@naver.com")
				.address("테스트주소")
				.registNumber("11111-111111")
				.contact("011-111-1111")
				.build();
		
		Customer result = customerRepository.save(modifyCustomer);
		
		Optional<Customer> customer = customerRepository.findById(result.getId());
		
		assertThat(customer.isPresent(), is(true));
		assertThat(testCustomer.getType(), is(result.getType()));
	}
}
