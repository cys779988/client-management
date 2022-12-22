package dev.be.customer.service.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.be.customer.validation.CustomerConstarint;
import dev.be.customer.validation.CustomerMarker;
import dev.be.customer.validation.RepresentiveMarker;
import dev.be.domain.model.CustomerEntity;
import dev.be.domain.model.CustomerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@CustomerConstarint
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

	@Schema(description = "고객번호")
	private Long id;
	
	@NotBlank(message = "한글이름은 필수값입니다.")
	@Schema(description = "한글이름", example = "홍길동")
	private String name;

	@NotBlank(groups = CustomerMarker.class, message = "외국인의 경우 영문이름은 필수값입니다.")
	@Schema(description = "영어이름", example = "GilDong Hong")
	private String englishName;

	@NotNull(message = "고택 타입은 필수값입니다.")
    @Schema(description = "고객타입", example = "KOREAN: 한국인, KOREAN_CORPORATION: 한국법인, FOREIGN: 외국인, FOREIGN_CORPORATION: 외국법인")
	private CustomerType type;
    
	@NotBlank(groups = CustomerMarker.class, message = "외국인의 경우 국적은 필수값입니다.")
	@Schema(description = "국적", example = "미국")
    private String nationality;
    
	@NotBlank(message = "주민등록번호(혹은 생년월일)은 필수값입니다.")
	@Schema(description = "주민등록번호(혹은 생년월일)", example = "1994-11-11\t\n123456-7897789")
    private String birthDate;
    
	@NotBlank(message = "이메일은 필수값입니다.")
	@Schema(description = "이메일", example = "hong@helpme.com")
    private String email;
    
	@NotBlank(message = "주소는 필수값입니다.")
	@Schema(description = "주소", example = "서울특별시 강남구 땡땡 0길 00")
    private String address;

	@NotBlank(message = "연락처는 필수값입니다.")
	@Schema(description = "연락처", example = "010-1111-1111")
    private String contact;
    
	@Valid
	@NotNull(groups = RepresentiveMarker.class, message = "법인 대표는 필수로 1명이상 등록해야 합니다.")
	private List<RepresentiveMember> representive;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<Long> removeRepresentive;
	
	public CustomerEntity toEntity() {
		return CustomerEntity.builder()
							.id(id)
							.name(name)
							.englishName(englishName)
							.type(type)
							.nationality(nationality)
							.birthDate(birthDate)
							.email(email)
							.address(address)
							.contact(contact)
							.build();
	}
}
