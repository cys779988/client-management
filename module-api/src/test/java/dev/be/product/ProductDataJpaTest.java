package dev.be.product;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
class ProductDataJpaTest {

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
		

	}
	
	@Test
	@DisplayName("제품 등록")
	public void saveProduct() {
		
		ProductEntity product = productRepository.save(ProductEntity.builder()
				.name("좋은제품")
				.price(BigInteger.valueOf(10000L))
				.quantity(50L)
				.build());
		
		assertThat(product.getId(), is(notNullValue()));
	}
	
	@Test
	@DisplayName("제품 조회")
	public void selectProduct() {
		List<ProductEntity> productEntityList = Arrays.asList(
							new ProductEntity(null, "테스트제품1", 50L, BigInteger.valueOf(10000L)),
							new ProductEntity(null, "테스트제품", 50L, BigInteger.valueOf(10000L)),
							new ProductEntity(null, "테스트제품3", 50L, BigInteger.valueOf(10000L)),
							new ProductEntity(null, "테스트제품4", 50L, BigInteger.valueOf(10000L)),
							new ProductEntity(null, "테스트제품5", 50L, BigInteger.valueOf(10000L)),
							new ProductEntity(null, "테스트제품6", 50L, BigInteger.valueOf(10000L)),
							new ProductEntity(null, "테스트제품7", 50L, BigInteger.valueOf(10000L)),
							new ProductEntity(null, "테스트제품8", 50L, BigInteger.valueOf(10000L)),
							new ProductEntity(null, "테스트제품9", 50L, BigInteger.valueOf(10000L)),
							new ProductEntity(null, "테스트제품10", 50L, BigInteger.valueOf(10000L))
							);
		
		productRepository.saveAll(productEntityList);
		
		em.flush();
		em.clear();
		
		Page<ProductEntity> pageProductEntity = productRepository.findAll(PageRequest.of(0, 8));
		
		assertThat(pageProductEntity.getTotalElements(), is(10L));
		assertThat(pageProductEntity.getTotalPages(), is(2));
	}
}
