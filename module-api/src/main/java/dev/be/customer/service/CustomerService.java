package dev.be.customer.service;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.be.customer.service.dto.CustomerRequest;
import dev.be.customer.service.dto.RepresentiveMember;
import dev.be.domain.model.CustomerEntity;
import dev.be.domain.model.CustomerType;
import dev.be.domain.model.RepresentiveEntity;
import dev.be.exception.BusinessException;
import dev.be.exception.ErrorCode;
import dev.be.repository.CustomerRepository;
import dev.be.repository.RepresentiveRepository;
import lombok.RequiredArgsConstructor;
import static dev.be.domain.model.CustomerType.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@RequiredArgsConstructor
@Service
public class CustomerService {
	private final CustomerRepository customerRepository;
	private final RepresentiveRepository representiveRepository;
	
	@Transactional
	public Long regist(@Valid CustomerRequest request) {
		CustomerType type = request.getType();
		CustomerEntity customer = customerRepository.save(request.toEntity());
		
		if(type == FOREIGN_CORPORATION || type == KOREAN_CORPORATION) {
			representiveRegist(customer, request.getRepresentive());
		}
		
		return customer.getId();
	}
	
	@Transactional
	public void modify(CustomerRequest request) {
		CustomerType type = request.getType();
		Long customerId = request.getId();
		
		Optional<CustomerEntity> entityWrapper = customerRepository.findById(customerId);
		
		if(entityWrapper.isPresent()) {
			compareAndDeleteRepresentive(entityWrapper.get().getType(), type, customerId);
		}
		
		CustomerEntity customer = customerRepository.save(request.toEntity());
		
		if(type == FOREIGN_CORPORATION || type == KOREAN_CORPORATION) {
			representiveRegist(customer, request.getRepresentive());
		}
	}
	
	private boolean compareAndDeleteRepresentive(CustomerType beforeType, CustomerType type, Long id) {
		if(beforeType != type && (beforeType == FOREIGN_CORPORATION || beforeType == KOREAN_CORPORATION)) {
			representiveRepository.deleteAllByCustomerId(id);
			return true;
		}
		return false;
	}
	
	private void representiveRegist(CustomerEntity customer, List<RepresentiveMember> representiveList) {
		List<RepresentiveEntity> representiveEntityList = representiveList.stream().map(i -> i.toEntity(customer)).collect(Collectors.toList());
		representiveRepository.saveAll(representiveEntityList);
	}

	public void delete(Long id) {
		try {
			representiveRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new BusinessException(ErrorCode.EMPTY_RESULT_DATA_ACCESS);
		}
	}

}
