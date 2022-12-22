package dev.be.customer.service;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
		
		registRepresentive(type, customer, request.getRepresentive());
		
		return customer.getId();
	}
	
	@Transactional
	public void update(@Valid CustomerRequest request) {
		CustomerType type = request.getType();
		Long customerId = request.getId();
		boolean isTypeChangedAndCorporation = false;
		Optional<CustomerEntity> entityWrapper = customerRepository.findById(customerId);
		
		if(entityWrapper.isPresent()) {
			isTypeChangedAndCorporation = compareAndDeleteRepresentive(entityWrapper.get().getType(), type, customerId);
		}
		
		CustomerEntity customer = customerRepository.save(request.toEntity());
		
		deleteRepresentive(isTypeChangedAndCorporation, request.getRemoveRepresentive());
		
		registRepresentive(type, customer, request.getRepresentive());
	}
	
	private boolean compareAndDeleteRepresentive(CustomerType beforeType, CustomerType type, Long id) {
		if(beforeType != type && beforeType.isCorporation()) {
			representiveRepository.deleteAllByCustomerId(id);
			return true;
		}
		return false;
	}
	
	private void registRepresentive(CustomerType type, CustomerEntity customer, List<RepresentiveMember> representiveList) {
		if(type.isCorporation() && !CollectionUtils.isEmpty(representiveList)) {
			List<RepresentiveEntity> representiveEntityList = representiveList.stream().map(i -> i.toEntity(customer)).collect(Collectors.toList());
			representiveRepository.saveAll(representiveEntityList);
		}
	}
	
	private void deleteRepresentive(boolean isTypeChangedAndCorporation, List<Long> list) {
		if(isTypeChangedAndCorporation == false && !CollectionUtils.isEmpty(list)) {
			List<RepresentiveEntity> entityList = representiveRepository.findAllById(list);
			representiveRepository.deleteInBatch(entityList);
		}
	}

	public void delete(Long id) {
		try {
			representiveRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new BusinessException(ErrorCode.EMPTY_RESULT_DATA_ACCESS);
		}
	}

}
