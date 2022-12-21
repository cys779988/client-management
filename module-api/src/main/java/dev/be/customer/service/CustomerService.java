package dev.be.customer.service;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.be.customer.service.dto.CustomerRequest;
import dev.be.customer.service.dto.RepresentiveMember;
import dev.be.domain.model.CustomerEntity;
import dev.be.domain.model.CustomerType;
import dev.be.domain.model.RepresentiveEntity;
import dev.be.repository.CustomerRepository;
import dev.be.repository.RepresentiveRepository;
import lombok.RequiredArgsConstructor;
import static dev.be.domain.model.CustomerType.*;

import java.util.List;
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
	
	private void representiveRegist(CustomerEntity customer, List<RepresentiveMember> representiveList) {
		List<RepresentiveEntity> representiveEntityList = representiveList.stream().map(i -> i.toEntity(customer)).collect(Collectors.toList());
		representiveRepository.saveAll(representiveEntityList);
	}
}
