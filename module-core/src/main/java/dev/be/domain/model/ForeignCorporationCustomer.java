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
@DiscriminatorValue("FOREIGN_CORPORATION")
@NoArgsConstructor
public class ForeignCorporationCustomer extends Foreign{
	@Column(name = "establishmentDate")
	private LocalDate establishmentDate;
	
	@Builder
	public ForeignCorporationCustomer(Long id, String name, String email, String address, String contact, String englishName, String nationality, LocalDate registDate) {
		super(id, name, email, address, contact, englishName, nationality);
		this.establishmentDate = registDate;
	}
}
