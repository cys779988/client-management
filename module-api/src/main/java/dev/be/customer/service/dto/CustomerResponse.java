package dev.be.customer.service.dto;

import dev.be.domain.model.CustomerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "판매 내역 등록 시 고객 조회 정보")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerResponse {
	
	@Schema(description = "고객 ID", example = "1")
	private Long id;
	
	@Schema(description = "고객 정보", example = "아마존(amazon@gmail.com)-외국법인")
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
