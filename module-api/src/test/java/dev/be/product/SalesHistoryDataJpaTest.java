package dev.be.product;
import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.*;

import java.math.BigInteger;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.be.domain.model.Customer;
import dev.be.domain.model.ForeignCustomer;
import dev.be.domain.model.ProductEntity;
import dev.be.repository.CustomerRepository;
import dev.be.repository.ProductRepository;
import dev.be.repository.SalesHistoryRepository;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class SalesHistoryDataJpaTest {

	@Autowired
	EntityManager em;
	
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	SalesHistoryRepository salesHistoryRepository;
	
	Customer customer;
	
	ProductEntity product;
	
	@BeforeEach
	private void setup() {
		customer = customerRepository.save(ForeignCustomer.builder()
				.name("이순신")
				.englishName("lee")
				.email("lee@naver.com")
				.address("테스트주소")
				.contact("010-0000-1111")
				.nationality("미국")
				.birthDate(LocalDate.of(1999, 12, 30))
				.build());
		
		product = productRepository.save(ProductEntity.builder()
				.name("좋은제품")
				.price(BigInteger.valueOf(10000L))
				.quantity(50L)
				.build());
	}
	
	@Test
	@DisplayName("판매 내역 등록")
	public void saveSalesHistory() {
		product.decrease(10L);
		productRepository.save(product);
		
		ProductEntity result = productRepository.findById(product.getId()).get();
		
		assertThat(product.getQuantity(), is(result.getQuantity()));
	}
}
