package dev.be.product;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
	
	private List<ProductEntity> getProducts() {
		List<ProductEntity> productList = Arrays.asList(
													ProductEntity.builder()
														.name("좋은제품1")
														.quantity(10L)
														.price(BigInteger.valueOf(100000L))
														.build(),
													ProductEntity.builder()
														.name("좋은제품2")
														.quantity(10L)
														.price(BigInteger.valueOf(100000L))
														.build(),
													ProductEntity.builder()
														.name("좋은제품3")
														.quantity(10L)
														.price(BigInteger.valueOf(100000L))
														.build());
		return productList;
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
	public void deleteProduct() {
		ProductRequest request = getProductRequest();
		productService.delete(request.getId());
	}

	@Test
	@DisplayName("제품 조회")
	public void findProducts() {
		Pageable page = PageRequest.of(0, 8);
		Page<ProductEntity> testData = new PageImpl<ProductEntity>(getProducts());
		when(productRepository.findAll(page)).thenReturn(testData);

		assertThat(productService.getProducts(page).getTotalElements(), is(testData.getTotalElements()));
	}
}
