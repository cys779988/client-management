package dev.be.customer.service.dto;

import dev.be.domain.model.CustomerBasicInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "고객 기본 정보 응답")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerResponse {
	
	@Schema(description = "고객 ID", example = "1")
	private Long id;
	
	@Schema(description = "고객 이름", example = "아마존")
	private String name;
	
	@Schema(description = "고객 이메일", example = "amazon@gmail.com")
	private String email;
	
	@Schema(description = "고객 타입", example = "외국법인")
	private String type;
	
	public static CustomerResponse of(CustomerBasicInfoResponse customer) {
		return new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), customer.getType().getName());
	}
}
