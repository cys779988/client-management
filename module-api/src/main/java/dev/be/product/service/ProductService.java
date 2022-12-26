package dev.be.product.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import dev.be.domain.model.PageResponse;
import dev.be.domain.model.ProductEntity;
import dev.be.exception.BusinessException;
import dev.be.exception.ErrorCode;
import dev.be.product.service.dto.ProductRequest;
import dev.be.repository.ProductRepository;
import dev.be.util.ExcelUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository productRepository;

	@Transactional
	public Long regist(ProductRequest request) {
		if(productRepository.findByName(request.getName()).isPresent()) {
			throw new BusinessException(ErrorCode.DUPLICATED_PRODUCT_NAME);
		}
		return productRepository.save(request.toEntity()).getId();
	}
	
	@Transactional
	public void update(ProductRequest request) {
		ProductEntity product = productRepository.findById(request.getId()).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PRODUCT));
		
		product.update(request.getName(), request.getQuantity(), request.getPrice());
		
		productRepository.save(product).getId();
	}

	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new BusinessException(ErrorCode.EMPTY_RESULT_DATA_ACCESS);
		}
	}

	public PageResponse<ProductEntity> getProducts(Pageable page) {
		Page<ProductEntity> pageProductEntity = productRepository.findAll(page);
		return PageResponse.of(pageProductEntity.getContent(), page.getPageNumber(), pageProductEntity.getTotalElements());
	}

	public InputStreamResource getProductsExcelFile(Pageable page) {
		Page<ProductEntity> pageProductEntity = productRepository.findAll(page);
		List<String> header = Arrays.asList("번호", "제품명", "재고", "금액");
		List<ProductEntity> productList = pageProductEntity.getContent();
		List<List<?>> excelBodyList = new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(productList)) {
			for (ProductEntity product : productList) {
				List<?> body = Arrays.asList(product.getId(), product.getName(), product.getQuantity(), product.getPrice());
				excelBodyList.add(body);
			}
		}
		
		return ExcelUtils.createExcelFileAsStream(header, excelBodyList);
	}
	
}
