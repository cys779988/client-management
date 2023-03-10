package dev.be.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import dev.be.domain.model.ProductEntity;
import dev.be.repository.ProductRepository;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@WithMockUser(roles = "ADMIN")
class ProductMvcTest {
	
	private static final String NAME = "좋은제품";
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	EntityManager em;
	
    @Test
    @DisplayName("제품등록 테스트")
    public void productRegistTest() throws Exception {

    	Map<String, String> param = new HashMap<>();
    	param.put("name", "테스트446874345제품");
    	param.put("quantity", "10");
    	param.put("price", "10000");
    	
        mockMvc.perform(post("/product")
    			.content(objectMapper.writeValueAsString(param))
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
        		.accept(MediaType.APPLICATION_JSON_VALUE))
	        .andDo(print())
			.andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("제품등록 재고 0개 유효성검사 실패 테스트")
    public void productRegistVaildationTest() throws Exception {
    	
    	Map<String, String> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("quantity", "0");
    	param.put("price", "10000");
    	
    	mockMvc.perform(post("/product")
				.content(objectMapper.writeValueAsString(param))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
	    	.andDo(print())
	    	.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("제품등록 재고 null 유효성검사 실패 테스트")
    public void productRegistVaildationTest2() throws Exception {
		
		Map<String, String> param = new HashMap<>();
		param.put("name", NAME);
		param.put("price", "10000");
		
		mockMvc.perform(post("/product")
				.content(objectMapper.writeValueAsString(param))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("제품등록 제품명 중복으로 인한 실패 테스트")
    public void productRegistDuplicatedNameTest() throws Exception {
    	productRepository.save(ProductEntity.builder().name(NAME).quantity(10L).price(BigInteger.valueOf(1000L)).build());
    	em.flush();
    	em.clear();
    	
    	Map<String, String> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("quantity", "10");
    	param.put("price", "10000");
    	
    	mockMvc.perform(post("/product")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("제품수정 테스트")
    public void productUpdateTest() throws Exception {
    	Long id = productRepository.save(ProductEntity.builder().name("테스트제품78331154").quantity(10L).price(BigInteger.valueOf(1000L)).build()).getId();
    	em.flush();
    	em.clear();
    	
		Map<String, String> param = new HashMap<>();
		param.put("name", NAME);
		param.put("quantity", "10");
		param.put("price", "10000");
		
		mockMvc.perform(put("/product/" + id)
				.content(objectMapper.writeValueAsString(param))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("제품삭제 테스트")
    public void productDeleteTest() throws Exception {
    	ProductEntity product = productRepository.save(ProductEntity.builder().name("테스트제품").quantity(10L).price(BigInteger.valueOf(1000L)).build());
		
		mockMvc.perform(delete("/product")
				.param("id", product.getId().toString())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("제품삭제 실패 테스트")
    public void productDeleteFailTest() throws Exception {
    	
    	mockMvc.perform(delete("/product")
    			.param("id", "999999")
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("제품조회 테스트")
    public void productFindTest() throws Exception {
    	
    	mockMvc.perform(get("/product")
    			.param("page", "1")
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isOk());
    }
}
