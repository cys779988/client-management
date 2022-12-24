package dev.be.customer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
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

import dev.be.customer.service.dto.RepresentiveMember;
import dev.be.domain.model.Customer;
import dev.be.domain.model.ForeignCorporationCustomer;
import dev.be.domain.model.RepresentiveEntity;
import dev.be.repository.CustomerRepository;
import dev.be.repository.RepresentiveRepository;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@WithMockUser(roles = "ADMIN")
class CustomerMvcTest {
	
	private static final String NAME = "홍길동";
	private static final String EMAIL = "hong1@helpme.com";
	private static final String ADDRESS = "서울특별시 강남구 땡땡 0길 00";
	private static final String CONTACT = "010-0000-0000";
	
	@Autowired
	private EntityManager em;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RepresentiveRepository representiveRepository;
	
    @Test
    @DisplayName("한국인 고객 등록 유효성검사 테스트")
    public void koreanCustomerRegistValidationTest() throws Exception {

    	Map<String, String> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("registNumber", "77777-111111");
    	param.put("type", "KOREAN");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	
        mockMvc.perform(post("/customer")
    			.content(objectMapper.writeValueAsString(param))
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
        		.accept(MediaType.APPLICATION_JSON_VALUE))
	        .andDo(print())
			.andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("한국인 고객 등록 유효성검사 테스트")
    public void koreanCustomerRegistValidationTest2() throws Exception {
    	
    	Map<String, String> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("registNumber", "77777-111111");
    	param.put("type", "KOREAN2");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	
    	mockMvc.perform(post("/customer")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("한국인 고객 등록 유효성검사 실패 테스트")
    public void koreanCustomerRegistValidationFailTest() throws Exception {
    	
    	Map<String, String> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("type", "KOREAN");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	
    	mockMvc.perform(post("/customer")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    			.andDo(print())
    			.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("외국인 고객 등록 유효성검사 테스트")
    public void foreignCustomerRegistValidationTest() throws Exception {
    	
    	Map<String, String> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("englishName", "test");
    	param.put("registDate", "1994-11-11");
    	param.put("nationality", "미국");
    	param.put("type", "FOREIGN");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	
    	mockMvc.perform(post("/customer")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("외국인 고객 등록 유효성검사 실패 테스트")
    public void foreignCustomerRegistValidationFailTest() throws Exception {
    	
    	Map<String, String> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("type", "FOREIGN");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	
    	mockMvc.perform(post("/customer")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    			.andDo(print())
    			.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("한국법인 고객 등록 유효성검사 테스트")
    public void koreanRepresentiveCustomerRegistValidationTest() throws Exception {

    	Map<String, Object> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("registNumber", "77777-111111");
    	param.put("type", "KOREAN_CORPORATION");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	param.put("representive", Arrays.asList(RepresentiveMember.builder().name(NAME).contact(CONTACT).build()));
    	
        mockMvc.perform(post("/customer")
    			.content(objectMapper.writeValueAsString(param))
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
        		.accept(MediaType.APPLICATION_JSON_VALUE))
	        .andDo(print())
			.andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("한국법인 고객 등록 대표법인 유효성 검사 실패 테스트")
    public void koreanRepresentiveCustomerRegistValidationFailTest() throws Exception {
    	
    	Map<String, Object> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("registNumber", "77777-111111");
    	param.put("type", "KOREAN_CORPORATION");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	param.put("representive", Arrays.asList(RepresentiveMember.builder().name(NAME).contact(null).build()));
    	
    	mockMvc.perform(post("/customer")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("한국법인 고객 등록 유효성검사 실패 테스트")
    public void koreanRepresentiveCustomerRegistValidationFailTest2() throws Exception {
    	
    	Map<String, String> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("registNumber", "77777-111111");
    	param.put("type", "KOREAN_CORPORATION");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	
    	mockMvc.perform(post("/customer")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    			.andDo(print())
    			.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("외국법인 고객 등록 유효성검사 테스트")
    public void foreignRepresentiveCustomerRegistValidationTest() throws Exception {

    	Map<String, Object> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("englishName", "test");
    	param.put("registDate", "1994-11-11");
    	param.put("type", "FOREIGN_CORPORATION");
    	param.put("nationality", "미국");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	param.put("representive", Arrays.asList(RepresentiveMember.builder().name(NAME).contact(CONTACT).build()));
    	
        mockMvc.perform(post("/customer")
    			.content(objectMapper.writeValueAsString(param))
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
        		.accept(MediaType.APPLICATION_JSON_VALUE))
	        .andDo(print())
			.andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("외국법인 고객 등록 대표법인 유효성 검사 실패 테스트")
    public void foreignRepresentiveCustomerRegistValidationFailTest() throws Exception {
    	
    	Map<String, Object> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("englishName", "test");
    	param.put("registDate", "1994-11-11");
    	param.put("type", "FOREIGN_CORPORATION");
    	param.put("nationality", "미국");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	param.put("representive", Arrays.asList(RepresentiveMember.builder().name(NAME).contact(null).build()));
    	
    	mockMvc.perform(post("/customer")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("외국법인 고객 등록 유효성검사 실패 테스트")
    public void foreignRepresentiveCustomerRegistValidationFailTest2() throws Exception {
    	
    	Map<String, String> param = new HashMap<>();
    	param.put("name", NAME);
    	param.put("registDate", "1994-11-11");
    	param.put("type", "FOREIGN_CORPORATION");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	
    	mockMvc.perform(post("/customer")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("일반 고객 정보 수정")
    public void updateCustomerTest() throws Exception {
    	Customer customerEntity = customerRepository.save(ForeignCorporationCustomer.builder()
        		.name(NAME)
        		.englishName("test")
        		.registDate(LocalDate.of(1994, 11, 11))
        		.nationality("미국")
        		.email(EMAIL)
        		.address(ADDRESS)
        		.contact(CONTACT)
        		.build());
    	
    	Map<String, String> param = new HashMap<>();
    	param.put("type", "FOREIGN");
    	param.put("name", NAME);
    	param.put("englishName", "test");
    	param.put("registDate", "1994-11-11");
    	param.put("email", EMAIL);
    	param.put("nationality", "미국");
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	
    	mockMvc.perform(put("/customer/" + customerEntity.getId().toString())
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("법인 고객 -> 일반 고객 정보 수정")
    public void updateCorporationCustomerToCustomerTest() throws Exception {
    	Customer customerEntity = customerRepository.save(ForeignCorporationCustomer.builder()
    			.name(NAME)
    			.englishName("test")
    			.registDate(LocalDate.of(1994, 11, 11))
    			.nationality("미국")
    			.email(EMAIL)
    			.address(ADDRESS)
    			.contact(CONTACT)
    			.build());
    	
    	representiveRepository.save(RepresentiveEntity.builder().name(NAME).contact(CONTACT).customer(customerEntity).build());
    	
    	Map<String, String> param = new HashMap<>();
    	param.put("type", "FOREIGN");
    	param.put("name", NAME);
    	param.put("englishName", "test");
    	param.put("registDate", "1994-11-11");
    	param.put("nationality", "미국");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	
    	mockMvc.perform(put("/customer/" + customerEntity.getId().toString())
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("법인 고객 -> 법인 고객 대표법인 정보 수정")
    public void updateCorporationCustomerUpdateRepresentiveTest() throws Exception {
    	Customer customerEntity = customerRepository.save(ForeignCorporationCustomer.builder()
    			.name(NAME)
    			.englishName("test")
    			.registDate(LocalDate.of(1994, 11, 11))
    			.nationality("미국")
    			.email(EMAIL)
    			.address(ADDRESS)
    			.contact(CONTACT)
    			.build());
    	
    	RepresentiveEntity representiveEntity = representiveRepository.save(RepresentiveEntity.builder().name(NAME).contact(CONTACT).customer(customerEntity).build());
    	
    	Map<String, Object> param = new HashMap<>();
    	param.put("type", "FOREIGN_CORPORATION");
    	param.put("name", NAME);
    	param.put("englishName", "test");
    	param.put("registDate", "1994-11-11");
    	param.put("nationality", "미국");
    	param.put("email", EMAIL);
    	param.put("address", ADDRESS);
    	param.put("contact", CONTACT);
    	param.put("representive", Arrays.asList(RepresentiveMember.builder().name(NAME).contact(CONTACT).build()));
    	param.put("removeRepresentive", Arrays.asList(representiveEntity.getId()));
    	
    	mockMvc.perform(put("/customer/" + customerEntity.getId().toString())
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("고객 정보 삭제 실패")
    public void deleteCustomerFailTest() throws Exception {
    	
    	mockMvc.perform(delete("/customer")
    			.param("id", "1")
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("고객 목록 조회")
    public void getCustomersTest() throws Exception {
    	customerRepository.save(ForeignCorporationCustomer.builder()
    			.name(NAME)
    			.englishName("test")
    			.registDate(LocalDate.of(1994, 11, 11))
    			.nationality("미국")
    			.email(EMAIL)
    			.address(ADDRESS)
    			.contact(CONTACT)
    			.build());
    	
    	em.flush();
    	em.clear();
    	
    	mockMvc.perform(get("/customer")
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isOk())
    	.andReturn();
    }
}
