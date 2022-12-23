package dev.be.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.be.domain.model.UserEntity;
import dev.be.exception.ErrorCode;
import dev.be.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class AuthTest {
	
	private static final String USER_ID = "hong1@helpme.com";
	private static final String PASSWORD = "Helpme12@@";
	private static final String NAME = "홍길동";
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
    @Test
    @DisplayName("회원가입 테스트")
    public void signupTest() throws Exception {
    	Map<String, String> param = new HashMap<>();
    	param.put("email", USER_ID);
    	param.put("password", PASSWORD);
    	param.put("name", NAME);
    	
        mockMvc.perform(post("/user/signup")
        			.content(objectMapper.writeValueAsString(param))
	        		.contentType(MediaType.APPLICATION_JSON_VALUE)
	        		.accept(MediaType.APPLICATION_JSON_VALUE))
		        .andDo(print())
				.andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("회원가입 유효성검증 테스트")
    public void signupExpect4xxErrorTest() throws Exception {
    	Map<String, String> param = new HashMap<>();
    	param.put("email", "test");
    	param.put("password", PASSWORD);
    	param.put("name", NAME);
    	
    	mockMvc.perform(post("/user/signup")
    			.content(objectMapper.writeValueAsString(param))
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().is4xxClientError());
    }
    
    @Test
    @DisplayName("로그인 테스트")
    public void loginTest() throws Exception {
    	String email = "yeong@helpme.com";
    	String password = "1111";
    	String name = "채영수";
    	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		userRepository.save(UserEntity.builder()
										.email(email)
										.password(passwordEncoder.encode(password))
										.name(name)
										.build());
		
    	MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    	param.add("email", email);
    	param.add("password", password);
    	
    	Object error = mockMvc.perform(post("/user/signin")
    			.params(param)
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().is(302))
    	.andReturn().getRequest().getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
    	
    	assertThat(Objects.isNull(error)).isEqualTo(true);
    	
    }
    
    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
    	String email = "yeong@helpme.com";
    	String password = "1111";
    	String name = "채영수";
    	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	userRepository.save(UserEntity.builder()
						    			.email(email)
						    			.password(passwordEncoder.encode(password))
						    			.name(name)
						    			.build());
    	
    	MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    	param.add("email", email);
    	param.add("password", "2222");
    	
    	Object sessionException = mockMvc.perform(post("/user/signin")
    			.params(param)
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    			.andDo(print())
    			.andExpect(status().is(302))
    			.andReturn().getRequest().getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
    	
    	Exception exception = (Exception)sessionException;
    	assertThat(ErrorCode.BAD_LOGIN_PASSWORD.getMessage().equals(exception.getMessage())).isEqualTo(true);
    	
    }
    
    @Test
    @DisplayName("로그인하지 않고 메인 페이지 접근")
    public void mainPage() throws Exception {
    	
    	mockMvc.perform(get("/main")
    			.contentType(MediaType.APPLICATION_JSON_VALUE)
    			.accept(MediaType.APPLICATION_JSON_VALUE))
    	.andDo(print())
    	.andExpect(status().isUnauthorized());
    }
    
    
}
