package dev.be.product.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import dev.be.domain.model.Customer;
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
	
	@Transactional
	public Long regist(SalesRequest request) {
		Long quantity = request.getQuantity();
		
		Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CUSTOMER));
		
		ProductEntity product = productRepository.findByIdWithOptimisticLock(request.getProductId()).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PRODUCT));
		
		try {
			product.decrease(quantity);
		} catch (RuntimeException e) {
			throw new BusinessException(ErrorCode.EXCEED_QUANTITY);
		}
		
		BigInteger totalPrice = BigInteger.valueOf(product.getPrice().intValue() * quantity);
		
		Long id = salesHistoryRepository.saveAndFlush(SalesHistoryEntity.builder()
													.customerName(customer.getName())
													.email(customer.getEmail())
													.productName(product.getName())
													.quantity(quantity)
													.price(totalPrice)
													.saleDate(request.getSaleDate())
													.build()).getId();
		
		return id;
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
