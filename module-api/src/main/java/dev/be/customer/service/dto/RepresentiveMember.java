package dev.be.customer.service.dto;

import javax.validation.constraints.NotBlank;

import dev.be.domain.model.CustomerEntity;
import dev.be.domain.model.RepresentiveEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepresentiveMember {
	
	private Long id;
	
	@NotBlank(message = "법인대표이름은 필수값입니다.")
	private String name;
	
	@NotBlank(message = "법인대표연락처는 필수값입니다.")
	private String contact;
	
	public RepresentiveEntity toEntity(CustomerEntity customerEntity) {
		return RepresentiveEntity.builder()
								.id(id)
								.customer(customerEntity)
								.name(name)
								.contact(contact)
								.build();
	}
}
