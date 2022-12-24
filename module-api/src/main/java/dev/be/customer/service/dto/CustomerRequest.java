package dev.be.customer.service.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.be.customer.validation.CustomerConstarint;
import dev.be.customer.validation.ForeignMarker;
import dev.be.customer.validation.KoreanMarker;
import dev.be.customer.validation.RepresentiveMarker;
import dev.be.domain.model.Customer;
import dev.be.domain.model.CustomerType;
import dev.be.domain.model.ForeignCorporationCustomer;
import dev.be.domain.model.ForeignCustomer;
import dev.be.domain.model.KoreanCorporationCustomer;
import dev.be.domain.model.KoreanCustomer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "고객 등록 요청")
@CustomerConstarint
@Data@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

	@Schema(description = "고객번호")
	private Long id;
	
	@NotBlank(message = "한글이름은 필수값입니다.")
	@Schema(description = "한글이름", example = "홍길동")
	private String name;

	@NotBlank(groups = ForeignMarker.class, message = "외국인의 경우 영문이름은 필수값입니다.")
	@Schema(description = "영어이름", example = "GilDong Hong")
	private String englishName;

	@NotNull(message = "고택 타입은 필수값입니다.")
    @Schema(description = "고객타입", example = "KOREAN_CORPORATION", enumAsRef = true)
	private CustomerType type;
    
	@NotBlank(groups = ForeignMarker.class, message = "외국인의 경우 국적은 필수값입니다.")
	@Schema(description = "국적", example = "미국")
    private String nationality;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(groups = ForeignMarker.class, message = "설립일자(혹은 생년월일)은 필수값입니다.")
	@Schema(description = "외국인, 외국법인일 경우 설립일자(혹은 생년월일) 등록", example = "1994-11-11")
    private LocalDate registDate;

	@NotBlank(groups = KoreanMarker.class, message = "주민등록번호(혹은 법인등록번호)은 필수값입니다.")
	@Schema(description = "한국인, 한국법인일 경우 주민등록번호(혹은 법인등록번호) 등록", example = "1564654-1534534")
	private String registNumber;
    
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
	
	
	public Customer toEntity() {
		switch (type) {
		case KOREAN:
			return KoreanCustomer.builder()
					.id(id)
					.name(name)
					.address(address)
					.contact(contact)
					.email(email)
					.registNumber(registNumber)
					.build();
		case KOREAN_CORPORATION:
			return KoreanCorporationCustomer.builder()
					.id(id)
					.name(name)
					.address(address)
					.contact(contact)
					.email(email)
					.registrationNumber(registNumber)
					.build();
		case FOREIGN:
			return ForeignCustomer.builder()
					.id(id)
					.name(name)
					.englishName(englishName)
					.nationality(nationality)
					.address(address)
					.contact(contact)
					.email(email)
					.birthDate(registDate)
					.build();
		case FOREIGN_CORPORATION:
			return ForeignCorporationCustomer.builder()
					.id(id)
					.name(name)
					.englishName(englishName)
					.nationality(nationality)
					.address(address)
					.contact(contact)
					.email(email)
					.registDate(registDate)
					.build();
		default:
			return null;
		}
	}
}
