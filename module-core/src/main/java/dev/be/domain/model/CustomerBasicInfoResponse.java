package dev.be.domain.model;


public interface CustomerBasicInfoResponse {
	Long getId();
	
	String getName();
	
	String getEmail();
	
	CustomerType getType();
}
