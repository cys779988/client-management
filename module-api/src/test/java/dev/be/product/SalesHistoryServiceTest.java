package dev.be.product;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.be.customer.service.CustomerService;
import dev.be.domain.model.Customer;
import dev.be.domain.model.KoreanCorporationCustomer;
import dev.be.domain.model.ProductEntity;
import dev.be.domain.model.SalesHistoryEntity;
import dev.be.exception.BusinessException;
import dev.be.exception.ErrorCode;
import dev.be.product.service.ProductService;
import dev.be.product.service.SalesHistoryService;
import dev.be.product.service.dto.SalesHistorySearchRequest;
import dev.be.product.service.dto.SalesRequest;
import dev.be.repository.CustomerRepository;
import dev.be.repository.ProductRepository;
import dev.be.repository.SalesHistoryRepository;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
class SalesHistoryServiceTest {
	
	@Autowired
	SalesHistoryService salesHistoryService;
	
	@Autowired
	ProductRepository productRepository;

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	CustomerService customerService;

	@Autowired
	ProductService productService;
	
	@Autowired
	SalesHistoryRepository salesHistoryRepository;
	
	@Autowired
	EntityManager em;

	Customer customer;
	
	ProductEntity product;
	
	@BeforeAll
    void init() {
    	
    	this.customer = customerRepository.save(KoreanCorporationCustomer.builder()
    			.id(1L)
    			.address("주소")
    			.registrationNumber("555555-1111111")
    			.contact("010-1111-1111")
    			.email("hong1@help-me.kr")
    			.name("홍길동")
    			.build());
    	
    	this.product = productRepository.save(ProductEntity.builder()
    			.name("좋은제품")
    			.quantity(10000L)
    			.price(BigInteger.valueOf(100000L))
    			.build());
    }
	
	@AfterAll
	void exit() {
		customerRepository.deleteById(customer.getId());
		productRepository.deleteById(product.getId());
	}
    
	@Test
	@DisplayName("판매 내역 등록 제품 없음 실패 테스트")
	public void registSalesHistoryInvalidProductId() {
		
		Long id = customer.getId();

		SalesRequest salesRequest = SalesRequest.builder()
				.customerId(id)
				.productId(100L)
				.quantity(100L)
				.saleDate(LocalDate.of(2022, 12, 10))
				.build();
		
		Throwable exception = assertThrows(BusinessException.class, () -> {
			salesHistoryService.regist(salesRequest);
		});
		
		assertThat(exception.getMessage(), is(ErrorCode.INVALID_PRODUCT.getMessage()));
	}
	
	@Test
	@DisplayName("판매 내역 등록 고객 없음 실패 테스트")
	public void registSalesHistoryInvalidCustomerId() {
		SalesRequest salesRequest = SalesRequest.builder()
				.customerId(100L)
				.productId(product.getId())
				.quantity(100L)
				.saleDate(LocalDate.of(2022, 12, 10))
				.build();
		
		Throwable exception = assertThrows(BusinessException.class, () -> {
			salesHistoryService.regist(salesRequest);
		});
		
		assertThat(exception.getMessage(), is(ErrorCode.INVALID_CUSTOMER.getMessage()));
	}
	
	@Test
	@DisplayName("판매 내역 등록 멀티스레드 환경에서 테스트")
	public void registSalesHistoryInMultiThread() throws InterruptedException {
		
		long quantity = 3999L;
		SalesRequest salesRequest = SalesRequest.builder()
				.customerId(customer.getId())
				.productId(product.getId())
				.quantity(quantity)
				.saleDate(LocalDate.of(2022, 12, 10))
				.build();
		
		AtomicInteger cnt = new AtomicInteger();
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		CountDownLatch countDownLatch = new CountDownLatch(10);
		
		for (int i = 0; i < 10; i++) {
			executorService.execute(() -> {
				try {
					salesHistoryService.regist(salesRequest);
					cnt.getAndIncrement();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();
		
		assertThat(productRepository.findById(product.getId()).get().getQuantity() > 0, is(true));
	}
	
	@Test
	@DisplayName("판매 내역 등록 낙관적 락 테스트")
	public void registSalesHistoryOptimisticLockingTest() throws InterruptedException {
		int numberOfThreads = 5;
		
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		
		SalesRequest salesRequest = SalesRequest.builder()
			.customerId(customer.getId())
			.productId(product.getId())
			.quantity(4000L)
			.saleDate(LocalDate.of(2022, 12, 10))
			.build();
		Future<?> future = executorService.submit(
		    () -> salesHistoryService.regist(salesRequest));
		Future<?> future2 = executorService.submit(
		    () -> salesHistoryService.regist(salesRequest));
		Future<?> future3 = executorService.submit(
		    () -> salesHistoryService.regist(salesRequest));
		Future<?> future4 = executorService.submit(
				() -> salesHistoryService.regist(salesRequest));
		Future<?> future5 = executorService.submit(
				() -> salesHistoryService.regist(salesRequest));
		
		Exception result = new Exception();
		
		try {
		    future.get();
		    future2.get();
		    future3.get();
		    future4.get();
		    future5.get();
		} catch (ExecutionException e) {
		    result = (Exception) e.getCause();
		}
		
		assertThat(result instanceof OptimisticLockingFailureException || result instanceof BusinessException, is(true));
	}
	
	@Test
	@DisplayName("제품 조회")
	public void findProducts() {
		salesHistoryRepository.saveAll(Arrays.asList(
				new SalesHistoryEntity(null, "고객1", "이메일", "제품1", 100L, BigInteger.valueOf(10000L), LocalDate.of(2022, 12, 22))
				, new SalesHistoryEntity(null, "고객1", "이메일", "제품1", 100L, BigInteger.valueOf(10000L), LocalDate.of(2022, 12, 22))
				, new SalesHistoryEntity(null, "고객1", "이메일", "제품1", 100L, BigInteger.valueOf(10000L), LocalDate.of(2022, 12, 22))
				, new SalesHistoryEntity(null, "고객2", "이메일", "제품1", 100L, BigInteger.valueOf(10000L), LocalDate.of(2022, 12, 22))
				, new SalesHistoryEntity(null, "고객3", "이메일", "제품1", 100L, BigInteger.valueOf(10000L), LocalDate.of(2022, 12, 22)))
				);
		
		
		SalesHistorySearchRequest searchRequest = SalesHistorySearchRequest.builder()
																			.customerName("고객1")
																			.build();
		long totalElements = salesHistoryService.getSalesHistory(PageRequest.of(0, 8), searchRequest).getTotalElements();
		
		assertThat(totalElements, is(3L));
		
		
	}
}
