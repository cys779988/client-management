package dev.be.domain.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse <T>{
	private List<T> contents;
	
	private int page;
	
	private long totalElements;
	
	public static <T> PageResponse<T> of(List<T> contents, int page, long totalElements) {
		return new PageResponse<T>(contents, page, totalElements);
	}
}
