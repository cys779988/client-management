package dev.be.domain.model;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public abstract class Foreign extends Customer{

	private String englishName;
	
	private String nationality;
	
	public Foreign(Long id, String name, String email, String address, String contact, String englishName, String nationality) {
		super(id, name, email, address, contact);
		this.englishName = englishName;
		this.nationality = nationality;
	}
}
