package dev.be.product.service;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import dev.be.domain.model.CustomerEntity;
import dev.be.domain.model.ProductEntity;
import dev.be.domain.model.SalesHistoryEntity;
import dev.be.exception.BusinessException;
import dev.be.exception.ErrorCode;
import dev.be.product.service.dto.SalesRequest;
import dev.be.repository.CustomerRepository;
import dev.be.repository.ProductRepository;
import dev.be.repository.SalesHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesHistoryService {

	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;
	private final SalesHistoryRepository salesHistoryRepository;
	
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void regist(SalesRequest request) {
		
		CustomerEntity customer = isValidCustomerAndReturnEntity(request.getCustomerId());
		
		ProductEntity product = isValidProductAndReturnEntity(request.getProductId());

		BigInteger totalPrice = isValidProductAndSaleProcess(product, request.getQuantity());
		
		salesHistoryRepository.saveAndFlush(SalesHistoryEntity.builder()
													.customerName(customer.getName())
													.email(customer.getEmail())
													.productName(product.getName())
													.quantity(request.getQuantity())
													.price(totalPrice)
													.saleDate(request.getSaleDate())
													.build());
	}

	private CustomerEntity isValidCustomerAndReturnEntity(Long customerId) {
		Optional<CustomerEntity> customerEntityWrapper = customerRepository.findById(customerId);
		if(!customerEntityWrapper.isPresent())
			throw new BusinessException(ErrorCode.INVALID_CUSTOMER);
		
		return customerEntityWrapper.get();
	}
	
	private ProductEntity isValidProductAndReturnEntity(Long productId) {
		Optional<ProductEntity> productEntityWrapper = productRepository.findById(productId);
		if(!productEntityWrapper.isPresent())
			throw new BusinessException(ErrorCode.INVALID_PRODUCT);
		
		return productEntityWrapper.get();
	}

	private BigInteger isValidProductAndSaleProcess(ProductEntity productEntity, Long quantity) {
		if(productEntity.getQuantity() < quantity) {
			throw new BusinessException(ErrorCode.EXCEED_QUANTITY);
		}
		productEntity.setQuantity(productEntity.getQuantity() - quantity);
		productRepository.saveAndFlush(productEntity);
		return BigInteger.valueOf(productEntity.getPrice().intValue() * quantity);
	}

}
