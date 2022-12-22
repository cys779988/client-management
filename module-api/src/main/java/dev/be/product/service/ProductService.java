package dev.be.product.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.be.exception.BusinessException;
import dev.be.exception.ErrorCode;
import dev.be.product.service.dto.ProductRequest;
import dev.be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository productRepository;

	@Transactional
	public Long regist(ProductRequest request) {
		if(productRepository.findByName(request.getName()).isPresent()) {
			throw new BusinessException(ErrorCode.DUPLICATED_NAME);
		}
		return productRepository.save(request.toEntity()).getId();
	}

	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new BusinessException(ErrorCode.EMPTY_RESULT_DATA_ACCESS);
		}
	}

}