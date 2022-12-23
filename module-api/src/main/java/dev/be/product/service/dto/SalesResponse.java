package dev.be.product.service.dto;

import java.math.BigInteger;
import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesResponse {
	
	private Long historyId;
	
	private Long customerName;
	
	private String email;
	
	private String productName;
	
	private Long quantity;
	
	private BigInteger price;
	
	private LocalDate saleDate;
}