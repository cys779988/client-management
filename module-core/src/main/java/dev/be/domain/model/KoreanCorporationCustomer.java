package dev.be.domain.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("KOREAN_CORPORATION")
@NoArgsConstructor
public class KoreanCorporationCustomer extends Customer{
	@Column(name = "registrationNumber")
	private String registrationNumber;
	
	@Builder
	public KoreanCorporationCustomer(Long id, String name, String email, String address, String contact, String registrationNumber) {
		super(id, name, email, address, contact);
		this.registrationNumber = registrationNumber;
	}
}
