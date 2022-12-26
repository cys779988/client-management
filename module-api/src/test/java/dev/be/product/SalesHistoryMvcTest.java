package dev.be.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.be.domain.model.Customer;
import dev.be.domain.model.ForeignCorporationCustomer;
import dev.be.domain.model.ProductEntity;
import dev.be.domain.model.SalesHistoryEntity;
import dev.be.repository.CustomerRepository;
import dev.be.repository.ProductRepository;
import dev.be.repository.SalesHistoryRepository;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@WithMockUser(roles = "ADMIN")
class SalesHistoryMvcTest {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ProductRepository productRepository;

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	SalesHistoryRepository salesHistoryRepository;
	
	@Autowired
	EntityManager em;
	
    @Test
    @DisplayName("제품 판매 내역 등록 테스트")
    public void salesRegistTest() throws Exception {
    	Customer customer = customerRepository.saveAndFlush(ForeignCorporationCustomer.builder()
														        		.name("홍길동")
														        		.englishName("test")
														        		.registDate(LocalDate.of(1994, 11, 11))
														        		.nationality("미국")
														        		.email("hong@help-me.kr")
														        		.address("서울특별시 강남구 땡땡")
														        		.contact("010-1111-2222")
														        		.build());
    	
    	ProductEntity product = productRepository.saveAndFlush(ProductEntity.builder()
													    			.name("좋은제품")
													    			.quantity(10L)
													    			.price(BigInteger.valueOf(1000L))
													    			.build());
    	
    	Map<String, Object> param = new HashMap<>();
    	param.put("customerId", customer.getId());
    	param.put("productId", product.getId());
    	param.put("quantity", "10");
    	param.put("saleDate", "2022-12-20");
    	
        mockMvc.perform(post("/sales")
    			.content(objectMapper.writeValueAsString(param))
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
        		.accept(MediaType.APPLICATION_JSON_VALUE))
	        .andDo(print())
			.andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("제품 판매 내역 등록 재고부족 실패 테스트")
    public void salesRegistFailTest1() throws Exception {
    	Customer customer = customerRepository.saveAndFlush(ForeignCorporationCustomer.builder()
    			.name("홍길동")
    			.englishName("test")
    			.registDate(LocalDate.of(1994, 11, 11))
    			.nationality("미국")
    			.email("hong@help-me.kr")
    			.address("서울특별시 강남구 땡땡")
    			.contact("010-1111-2222")
    			.build());
    	
    	ProductEntity product = productRepository.saveAndFlush(ProductEntity.builder()
    			.name("좋은제품")
    			.quantity(10L)
    			.price(BigInteger.valueOf(1000L))
    			.build());
    	
    	Map<String, Object> param = new HashMap<>();
    	param.put("customerId", customer.getId());
    	param.put("productId", product.getId());
    	param.put("quantity", "11");
    	param.put("saleDate", "2022-12-20");
    	
    	mockMvc.perform(post("/sales")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("제품 판매 내역 등록 잘못된 제품으로 실패 테스트")
    public void salesRegistFailTest2() throws Exception {
    	Customer customer = customerRepository.saveAndFlush(ForeignCorporationCustomer.builder()
    			.name("홍길동")
    			.englishName("test")
    			.registDate(LocalDate.of(1994, 11, 11))
    			.nationality("미국")
    			.email("hong@help-me.kr")
    			.address("서울특별시 강남구 땡땡")
    			.contact("010-1111-2222")
    			.build());
    	
    	Map<String, Object> param = new HashMap<>();
    	param.put("customerId", customer.getId());
    	param.put("productId", 100L);
    	param.put("quantity", "10");
    	param.put("saleDate", "2022-12-20");
    	
    	mockMvc.perform(post("/sales")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("제품 판매 내역 고객명으로 조회 테스트")
    public void getSalesHistoryTest1() throws Exception {
    	salesHistoryRepository.saveAndFlush(SalesHistoryEntity.builder()
						.customerName("강감찬")
						.email("k@help-me.kr")
						.productName("좋은제품")
						.quantity(10L)
						.price(BigInteger.valueOf(100000L))
						.saleDate(LocalDate.now())
						.build());
    	salesHistoryRepository.saveAndFlush(SalesHistoryEntity.builder()
		    			.customerName("이순신")
		    			.email("l@help-me.kr")
		    			.productName("좋은제품")
		    			.quantity(10L)
		    			.price(BigInteger.valueOf(100000L))
		    			.saleDate(LocalDate.now())
		    			.build());
    	
    	Map<String, Object> param = new HashMap<>();
    	param.put("customerName", "강감찬");
    	
    	mockMvc.perform(get("/sales")
    			.param("customerName", "강감찬")
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("제품 판매 내역 고객명, 제품명으로 조회 테스트")
    public void getSalesHistoryTest2() throws Exception {
    	salesHistoryRepository.saveAndFlush(SalesHistoryEntity.builder()
    			.customerName("강감찬")
    			.email("k@help-me.kr")
    			.productName("좋은제품")
    			.quantity(10L)
    			.price(BigInteger.valueOf(100000L))
    			.saleDate(LocalDate.now())
    			.build());
    	salesHistoryRepository.saveAndFlush(SalesHistoryEntity.builder()
    			.customerName("강감찬")
    			.email("k@help-me.kr")
    			.productName("나쁜제품")
    			.quantity(10L)
    			.price(BigInteger.valueOf(100000L))
    			.saleDate(LocalDate.now())
    			.build());
    	salesHistoryRepository.saveAndFlush(SalesHistoryEntity.builder()
    			.customerName("이순신")
    			.email("l@help-me.kr")
    			.productName("좋은제품")
    			.quantity(10L)
    			.price(BigInteger.valueOf(100000L))
    			.saleDate(LocalDate.now())
    			.build());
    	
    	mockMvc.perform(get("/sales")
    			.param("customerName", "강감찬")
    			.param("productName", "나쁜제품")
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("제품 판매 내역 판매일자로 조회 테스트")
    public void getSalesHistoryTest3() throws Exception {
    	salesHistoryRepository.saveAndFlush(SalesHistoryEntity.builder()
				.customerName("강감찬")
				.email("k@help-me.kr")
				.productName("좋은제품")
				.quantity(10L)
				.price(BigInteger.valueOf(100000L))
				.saleDate(LocalDate.of(2022, 10, 10))
				.build());
    	
    	salesHistoryRepository.saveAndFlush(SalesHistoryEntity.builder()
	    			.customerName("이순신")
	    			.email("l@help-me.kr")
	    			.productName("좋은제품")
	    			.quantity(10L)
	    			.price(BigInteger.valueOf(100000L))
	    			.saleDate(LocalDate.of(2022, 12, 16))
	    			.build());
    	
    	mockMvc.perform(get("/sales")
    			.param("salesStartDate", "2022-12-15")
    			.param("salesEndDate", "2022-12-20")
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isOk());
    }
}
