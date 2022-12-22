package dev.be.customer.service.dto;

import dev.be.domain.model.CustomerEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerResponse {
	private Long id;
	private String name;
	
	public static CustomerResponse of(CustomerEntity entity) {
		StringBuilder sb = new StringBuilder();
		sb.append(entity.getName());
		sb.append("(");
		sb.append(entity.getEmail());
		sb.append(")-");
		sb.append(entity.getType().getValue());
		
		return new CustomerResponse(entity.getId(), sb.toString());
	}
}
