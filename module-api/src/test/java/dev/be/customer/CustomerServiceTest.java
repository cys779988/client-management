package dev.be.customer;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.be.customer.service.CustomerService;
import dev.be.customer.service.dto.CustomerRequest;
import dev.be.domain.model.CustomerEntity;
import dev.be.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@InjectMocks
	private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;	

    private static final String ID = "admin";
    private CustomerEntity getCustomer() {
    	CustomerEntity customer = CustomerEntity.builder()
    			.id(1L)
    			.address("주소")
    			.birthDate("")
    			.contact(ID)
    			.email(ID)
    			.englishName(ID)
    			.name(ID)
    			.build();
    	return customer;
    }
    
    @Test
    public void registCustomer() {
    	CustomerRequest request = CustomerRequest.builder()
						    					.address(ID)
						    					.birthDate(ID)
						    					.build();
    	customerService.regist(request);
    	
    	//when(boardRepository.findByTitleContaining(page, null)).thenReturn(testData);
    	
    }
}
 