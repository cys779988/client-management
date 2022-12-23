package dev.be.product;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import dev.be.domain.model.CustomerEntity;
import dev.be.domain.model.CustomerType;
import dev.be.domain.model.ProductEntity;
import dev.be.domain.model.SalesHistoryEntity;
import dev.be.exception.BusinessException;
import dev.be.exception.ErrorCode;
import dev.be.product.service.SalesHistoryService;
import dev.be.product.service.SalesHistorySpecification;
import dev.be.product.service.dto.SalesHistorySearchRequest;
import dev.be.product.service.dto.SalesRequest;
import dev.be.repository.CustomerRepository;
import dev.be.repository.ProductRepository;
import dev.be.repository.SalesHistoryRepository;

@ExtendWith(MockitoExtension.class)
class SalesHistoryServiceTest {

	@InjectMocks
	private SalesHistoryService salesHistoryService;

	@Mock
	private SalesHistoryRepository salesHistoryRepository;
	
	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private ProductRepository productRepository;

	private SalesRequest getSalesRequest() {
		SalesRequest request = SalesRequest.builder()
				.customerId(1L)
				.productId(1L)
				.quantity(100L)
				.saleDate(LocalDate.of(2022, 12, 10))
				.build();
		return request;
	}
	
	private SalesHistorySearchRequest getHistoryRequest() {
		SalesHistorySearchRequest request = SalesHistorySearchRequest.builder()
				.customerName("이순신")
				.productName("거북선")
				.salesStartDate(LocalDate.of(2022, 12, 10))
				.salesEndDate(LocalDate.of(2022, 12, 16))
				.build();
		return request;
	}
	
    private CustomerEntity getCustomer() {
    	CustomerEntity customer = CustomerEntity.builder()
    			.id(1L)
    			.type(CustomerType.KOREAN_CORPORATION)
    			.address("주소")
    			.birthDate("1994-11-11")
    			.contact("010-1111-1111")
    			.email("hong1@help-me.kr")
    			.name("홍길동")
    			.build();
    	return customer;
    }
    
	private ProductEntity getProduct() {
		ProductEntity product = ProductEntity.builder()
				.name("좋은제품")
				.quantity(10L)
				.price(BigInteger.valueOf(100000L))
				.build();
		return product;
	}
    
	private SalesHistoryEntity getSalesHistoryEntity() {
		return new SalesHistoryEntity(1L, "홍길동", "test@naver.com", "좋은제품", 1L, BigInteger.valueOf(100000L), LocalDate.now());
	}
	
	private List<SalesHistoryEntity> getSalesHistoryEntityList() {
		List<SalesHistoryEntity> productList = Arrays.asList(
												new SalesHistoryEntity(1L, "홍길동", "test@help-me.kr", "좋은제품", 1L, BigInteger.valueOf(100000L), LocalDate.now()),
												new SalesHistoryEntity(1L, "홍길동", "test@help-me.kr", "좋은제품", 1L, BigInteger.valueOf(100000L), LocalDate.now()),
												new SalesHistoryEntity(1L, "홍길동", "test@help-me.kr", "좋은제품", 1L, BigInteger.valueOf(100000L), LocalDate.now()),
												new SalesHistoryEntity(1L, "홍길동", "test@help-me.kr", "좋은제품", 1L, BigInteger.valueOf(100000L), LocalDate.now())
												);
		return productList;
	}

	@Test
	@DisplayName("판매 내역 등록 제품 없음 실패 테스트")
	public void registSalesHistoryInvalidProductId() {
		SalesRequest salesRequest =  getSalesRequest();
		SalesHistoryEntity entity = getSalesHistoryEntity();
		lenient().when(salesHistoryRepository.save(any(SalesHistoryEntity.class))).thenReturn(entity);
		lenient().when(customerRepository.findById(salesRequest.getCustomerId())).thenReturn(Optional.of(getCustomer()));
		lenient().when(productRepository.findById(salesRequest.getProductId())).thenReturn(Optional.empty());

		Throwable exception = assertThrows(BusinessException.class, () -> {
			salesHistoryService.regist(salesRequest);
		});
		
		assertThat(exception.getMessage(), is(ErrorCode.INVALID_PRODUCT.getMessage()));
	}
	
	@Test
	@DisplayName("판매 내역 등록 고객 없음 실패 테스트")
	public void registSalesHistoryInvalidCustomerId() {
		SalesRequest salesRequest =  getSalesRequest();
		SalesHistoryEntity entity = getSalesHistoryEntity();
		lenient().when(salesHistoryRepository.save(any(SalesHistoryEntity.class))).thenReturn(entity);
		lenient().when(customerRepository.findById(salesRequest.getCustomerId())).thenReturn(Optional.empty());
		lenient().when(productRepository.findById(salesRequest.getProductId())).thenReturn(Optional.of(getProduct()));
		
		Throwable exception = assertThrows(BusinessException.class, () -> {
			salesHistoryService.regist(getSalesRequest());
		});
		
		assertThat(exception.getMessage(), is(ErrorCode.INVALID_CUSTOMER.getMessage()));
	}
	
	@Test
	@DisplayName("판매 내역 등록 제품 재고 부족 실패 테스트")
	public void registSalesHistoryExceedQuantity() {
		SalesRequest salesRequest =  getSalesRequest();
		SalesHistoryEntity entity = getSalesHistoryEntity();
		lenient().when(salesHistoryRepository.save(any(SalesHistoryEntity.class))).thenReturn(entity);
		lenient().when(customerRepository.findById(salesRequest.getCustomerId())).thenReturn(Optional.of(getCustomer()));
		lenient().when(productRepository.findById(salesRequest.getProductId())).thenReturn(Optional.of(getProduct()));
		
		Throwable exception = assertThrows(BusinessException.class, () -> {
			salesHistoryService.regist(getSalesRequest());
		});
		
		assertThat(exception.getMessage(), is(ErrorCode.EXCEED_QUANTITY.getMessage()));
	}

	@SuppressWarnings("static-access")
	@Test
	@DisplayName("제품 조회")
	public void findProducts() {
		Pageable page = PageRequest.of(0, 8);
		SalesHistorySearchRequest request = getHistoryRequest();
		Page<SalesHistoryEntity> testData = new PageImpl<SalesHistoryEntity>(getSalesHistoryEntityList());
		try (MockedStatic<SalesHistorySpecification> util = Mockito.mockStatic(SalesHistorySpecification.class)) {
			when(SalesHistorySpecification.getSpecification(request)).thenReturn(null);
			lenient().when(salesHistoryRepository.findAll(new SalesHistorySpecification().getSpecification(request) , page)).thenReturn(testData);

			assertThat(salesHistoryService.getSalesHistory(page, getHistoryRequest()).getTotalElements(), is(testData.getTotalElements()));
		}
		
	}
}
