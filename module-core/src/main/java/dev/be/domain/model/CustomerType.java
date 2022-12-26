package dev.be.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerType {
	KOREAN("한국인", "KOREAN"), KOREAN_CORPORATION("한국법인", "KOREAN_CORPORATION"), FOREIGN("외국인", "FOREIGN"), FOREIGN_CORPORATION("외국법인", "FOREIGN_CORPORATION");
	
	private final String name;
	
	private final String value;
	
	@JsonCreator
	public static CustomerType from(String s) {
		return CustomerType.valueOf(s.toUpperCase());
	}
	
	public boolean isCorporation() {
		return this == KOREAN_CORPORATION || this == FOREIGN_CORPORATION;
	}
}