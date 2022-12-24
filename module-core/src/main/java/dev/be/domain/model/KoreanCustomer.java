package dev.be.domain.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("KOREAN")
@NoArgsConstructor
public class KoreanCustomer extends Customer{
	@Column(name = "residentNumber")
	private String residentNumber;
	
	@Builder
	public KoreanCustomer(Long id, String name, String email, String address, String contact, String registNumber) {
		super(id, name, email, address, contact);
		this.residentNumber = registNumber;
	}
}
