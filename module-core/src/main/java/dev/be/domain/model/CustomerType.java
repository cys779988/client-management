package dev.be.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerType {
	KOREAN("korean"), KOREAN_CORPORATION("korean_corporation"), FOREIGN("foreign"), FOREIGN_CORPORATION("foreign_corporation");
	
	private final String type;
	
	@JsonCreator
	public static CustomerType from(String s) {
		return CustomerType.valueOf(s.toUpperCase());
	}
}