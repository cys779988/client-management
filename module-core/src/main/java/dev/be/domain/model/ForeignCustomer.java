package dev.be.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("FOREIGN")
@NoArgsConstructor
public class ForeignCustomer extends Foreign{
	@Column(name = "birthDate")
	private LocalDate birthDate;
	
	@Builder
	public ForeignCustomer(Long id, String name, String email, String address, String contact, String englishName, String nationality, LocalDate birthDate) {
		super(id, name, email, address, contact, englishName, nationality);
		this.birthDate = birthDate;
	}
}
