package dev.be.customer.service;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import dev.be.customer.service.dto.CustomerRequest;
import dev.be.customer.service.dto.CustomerResponse;
import dev.be.domain.model.Customer;
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
		if(isDuplicatedCondition(request)) {
			throw new BusinessException(ErrorCode.DUPLICATED_CUSTOMER);
		}
		
		Customer customer = customerRepository.save(request.toEntity());

		registRepresentive(request, customer);
		
		return customer.getId();
	}
	
	private boolean isDuplicatedCondition(CustomerRequest request) {
		CustomerType type = request.getType();
		
		switch (type) {
		case KOREAN:
			return customerRepository.findByNameAndResidentNumber(request.getName(), request.getRegistNumber()).isPresent();
		case KOREAN_CORPORATION:
			return customerRepository.findByNameAndRegistrationNumber(request.getName(), request.getRegistNumber()).isPresent();
		case FOREIGN:
			return customerRepository.findByNameAndBirthDate(request.getName(), request.getRegistDate()).isPresent();
		case FOREIGN_CORPORATION:
			return customerRepository.findByNameAndEstablishmentDate(request.getName(), request.getRegistDate()).isPresent();
		}
		return false;
	}

	@Transactional
	public void update(@Valid CustomerRequest request) {
		Optional<Customer> entityWrapper = customerRepository.findById(request.getId());
		
		if(!entityWrapper.isPresent()) {
			throw new BusinessException(ErrorCode.INVALID_CUSTOMER);
		}
		
		Customer customer = request.toEntity();
		
		if(compareCustomerTypeAndDeleteCustomer(request, customer, entityWrapper.get())) {
			deleteRepresentive(request.getRemoveRepresentive());
		}
		
		customer = customerRepository.save(customer);
		registRepresentive(request, customer);
	}
	
	private boolean compareCustomerTypeAndDeleteCustomer(CustomerRequest request, Customer customer, Customer beforeEntity) {
		if(beforeEntity.getType() != request.getType()) {
			customerRepository.delete(beforeEntity);
			return false;
		}
		return true;
	}
	
	private void registRepresentive(CustomerRequest request, Customer customer) {
		if(request.getType().isCorporation()) {
			List<RepresentiveEntity> representiveEntityList = request.getRepresentive().stream().map(i -> i.toEntity(customer)).collect(Collectors.toList());
			representiveRepository.saveAll(representiveEntityList);
		}
	}
	
	private void deleteRepresentive(List<Long> list) {
		if(!CollectionUtils.isEmpty(list)) {
			List<RepresentiveEntity> entityList = representiveRepository.findAllById(list);
			representiveRepository.deleteInBatch(entityList);
		}
	}

	public void delete(Long id) {
		try {
			customerRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new BusinessException(ErrorCode.EMPTY_RESULT_DATA_ACCESS);
		}
	}

	public List<CustomerResponse> getCustomersBasicInfo() {
		return customerRepository.findAllBy().stream().map(CustomerResponse::of).collect(Collectors.toList());
	}
	
	public List<Customer> getCustomers() {
		return customerRepository.findAll();
	}

	public Customer getCustomer(Long id) {
		Optional<Customer> entityWrapper = customerRepository.findById(id);
		
		if(!entityWrapper.isPresent()) {
			throw new BusinessException(ErrorCode.INVALID_CUSTOMER);
		}
		
		return entityWrapper.get();
	}

}
