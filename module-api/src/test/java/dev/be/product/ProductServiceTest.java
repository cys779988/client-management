package dev.be.product;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.be.domain.model.ProductEntity;
import dev.be.product.service.ProductService;
import dev.be.product.service.dto.ProductRequest;
import dev.be.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

    @Mock
    private ProductRepository productRepository;	
    
    private ProductRequest getProductRequest() {
    	ProductRequest request = ProductRequest.builder()
    			.id(1L)
    			.name("좋은제품")
    			.quantity(10L)
    			.price(BigInteger.valueOf(100000L))
    			.build();
    	return request;
    }
    
    private ProductEntity getProduct() {
    	ProductEntity product = ProductEntity.builder()
    			.name("좋은제품")
    			.quantity(10L)
    			.price(BigInteger.valueOf(100000L))
    			.build();
    	return product;
    }
    
    @Test
    @DisplayName("제품 등록")
    public void registProduct() {
    	ProductEntity entity = getProduct();
    	when(productRepository.save(any(ProductEntity.class))).thenReturn(entity);

    	assertThat(productService.regist(getProductRequest()), is(entity.getId()));
    }
    
    @Test
    @DisplayName("제품 삭제")
    public void deleteCustomer() {
    	ProductRequest request = getProductRequest();
    	productService.delete(request.getId());
    }
}
 