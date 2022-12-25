package dev.be.customer.service.dto;

import javax.validation.constraints.NotBlank;

import dev.be.domain.model.Customer;
import dev.be.domain.model.RepresentiveEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "법인 대표 정보")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepresentiveMember {
	
	@Schema(description = "법인대표 ID", example = "1")
	private Long id;
	
	@Schema(description = "법인대표이름", example = "이순신")
	@NotBlank(message = "법인대표이름은 필수값입니다.")
	private String name;
	
	@Schema(description = "법인대표연락처", example = "010-1111-1111")
	@NotBlank(message = "법인대표연락처는 필수값입니다.")
	private String contact;
	
	public RepresentiveEntity toEntity(Customer customer) {
		return RepresentiveEntity.builder()
								.id(id)
								.customer(customer)
								.name(name)
								.contact(contact)
								.build();
	}
}
