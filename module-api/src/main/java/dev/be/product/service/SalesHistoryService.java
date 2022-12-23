package dev.be.product.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import dev.be.domain.model.CustomerEntity;
import dev.be.domain.model.PageResponse;
import dev.be.domain.model.ProductEntity;
import dev.be.domain.model.SalesHistoryEntity;
import dev.be.exception.BusinessException;
import dev.be.exception.ErrorCode;
import dev.be.product.service.dto.SalesHistorySearchRequest;
import dev.be.product.service.dto.SalesRequest;
import dev.be.repository.CustomerRepository;
import dev.be.repository.ProductRepository;
import dev.be.repository.SalesHistoryRepository;
import dev.be.util.ExcelUtils;
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

	public PageResponse<SalesHistoryEntity> getSalesHistory(Pageable page, SalesHistorySearchRequest request) {
		Page<SalesHistoryEntity> pageHistoryEntity = salesHistoryRepository.findAll(SalesHistorySpecification.getSpecification(request), page);
		
		return PageResponse.of(pageHistoryEntity.getContent(), page.getPageNumber(), pageHistoryEntity.getTotalElements());
	}

	public InputStreamResource getSalesHistoryExcelFile(Pageable page, SalesHistorySearchRequest request) {
		Page<SalesHistoryEntity> pageHistoryEntity = salesHistoryRepository.findAll(SalesHistorySpecification.getSpecification(request), page);		
		List<String> header = Arrays.asList("번호", "이름", "이메일", "제품명", "수량", "금액", "판매일자");
		List<SalesHistoryEntity> salesHistoryList = pageHistoryEntity.getContent();
		List<List<?>> excelBodyList = new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(salesHistoryList)) {
			for (SalesHistoryEntity history : salesHistoryList) {
				List<?> body = Arrays.asList(history.getId(), history.getCustomerName(), history.getEmail(), history.getProductName()
											, history.getQuantity(), history.getPrice(), history.getSaleDate());
				excelBodyList.add(body);
			}
		}
		
		return ExcelUtils.createExcelFileAsStream(header, excelBodyList);
	}

}
