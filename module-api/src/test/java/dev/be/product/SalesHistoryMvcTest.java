package dev.be.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
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

import dev.be.domain.model.CustomerEntity;
import dev.be.domain.model.CustomerType;
import dev.be.domain.model.ProductEntity;
import dev.be.repository.CustomerRepository;
import dev.be.repository.ProductRepository;

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
	EntityManager em;
	
    @Test
    @DisplayName("제품 판매 내역 등록 테스트")
    public void salesRegistTest() throws Exception {
    	CustomerEntity customer = customerRepository.saveAndFlush(CustomerEntity.builder()
														        		.type(CustomerType.FOREIGN_CORPORATION)
														        		.name("홍길동")
														        		.englishName("test")
														        		.birthDate("1994-11-11")
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
}
