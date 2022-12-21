package dev.be.customer;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.be.customer.service.CustomerService;
import dev.be.customer.service.dto.CustomerRequest;
import dev.be.customer.service.dto.RepresentiveMember;
import dev.be.domain.model.CustomerEntity;
import dev.be.domain.model.CustomerType;
import dev.be.domain.model.RepresentiveEntity;
import dev.be.repository.CustomerRepository;
import dev.be.repository.RepresentiveRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@InjectMocks
	private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;	
    
    @Mock
    private RepresentiveRepository representiveRepository;	
    
    private CustomerRequest getCustomerRequest() {
    	CustomerRequest request = CustomerRequest.builder()
    			.type(CustomerType.KOREAN)
    			.address("주소")
    			.birthDate("1994-11-11")
    			.contact("010-1111-1111")
    			.email("hong1@help-me.kr")
    			.name("홍길동")
    			.build();
    	return request;
    }
    
    private CustomerRequest getSavedCustomerRequest() {
    	CustomerRequest request = CustomerRequest.builder()
    			.id(1L)
    			.type(CustomerType.KOREAN)
    			.address("주소")
    			.birthDate("1994-11-11")
    			.contact("010-1111-1111")
    			.email("hong1@help-me.kr")
    			.name("홍길동")
    			.build();
    	return request;
    }
    
    private CustomerRequest getCorporationCustomerRequest() {
    	CustomerRequest request = CustomerRequest.builder()
    			.id(1L)
    			.type(CustomerType.KOREAN_CORPORATION)
    			.address("주소")
    			.birthDate("1994-11-11")
    			.contact("010-1111-1111")
    			.email("hong1@help-me.kr")
    			.name("홍길동")
    			.representive(Arrays.asList(RepresentiveMember.builder().name("강감찬").contact("010-2222-2233").build()))
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
    
    private RepresentiveEntity getRepresentive() {
    	RepresentiveEntity representive = RepresentiveEntity.builder()
    			.id(1L)
    			.customer(getCustomer())
    			.contact("010-1111-1111")
    			.name("홍길동")
    			.build();
    	return representive;
    }
    
    @Test
    @DisplayName("고객 등록")
    public void registCustomer() {
    	CustomerEntity entity = getCustomer();
    	when(customerRepository.save(any(CustomerEntity.class))).thenReturn(entity);

    	assertThat(customerService.regist(getCustomerRequest()), is(entity.getId()));
    }
    
    @Test
    @DisplayName("법인고객정보 수정")
    public void modifyCustomer() {
    	CustomerEntity entity = getCustomer();
    	CustomerRequest request = getSavedCustomerRequest();
    	when(customerRepository.findById(request.getId())).thenReturn(Optional.of(entity));
    	when(customerRepository.save(any(CustomerEntity.class))).thenReturn(entity);
    	
    	customerService.modify(request);
    }
}
 