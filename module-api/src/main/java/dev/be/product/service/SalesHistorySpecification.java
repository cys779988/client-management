package dev.be.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import dev.be.domain.model.SalesHistoryEntity;
import dev.be.product.service.dto.SalesHistorySearchRequest;

@Component
public class SalesHistorySpecification {
	public static Specification<SalesHistoryEntity> getSpecification(SalesHistorySearchRequest request) {
		return (root, query, builder) -> {
			List<Predicate> predicate = getPredicateWithKeyword(request, root, builder);
			return builder.and(predicate.toArray(new Predicate[0]));
		};
	}
	
	private static List<Predicate> getPredicateWithKeyword(SalesHistorySearchRequest request, Root<SalesHistoryEntity> root, CriteriaBuilder builder) {
		List<Predicate> predicate = new ArrayList<>();
		if(StringUtils.hasText(request.getCustomerName())) {
			predicate.add(builder.equal(root.get("customerName"), request.getCustomerName()));
		}
		
		if(StringUtils.hasText(request.getProductName())) {
			predicate.add(builder.equal(root.get("productName"), request.getProductName()));
		}
		
		if(Objects.nonNull(request.getSalesEndDate()) && Objects.nonNull(request.getSalesStartDate())) {
			predicate.add(builder.between(root.get("saleDate"), request.getSalesStartDate(), request.getSalesEndDate()));
		}
		
		return predicate;
	}
}
