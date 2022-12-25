package dev.be.product.service.dto;

import java.math.BigInteger;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "제품 판매 정보")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesResponse {
	
	@Schema(description = "판매기록 ID", example = "1")
	private Long historyId;
	
	@Schema(description = "고객명", example = "이순신")
	private Long customerName;
	
	@Schema(description = "고객이메일", example = "rte@naver.com")
	private String email;
	
	@Schema(description = "제품명", example = "거북선")
	private String productName;
	
	@Schema(description = "수량", example = "10")
	private Long quantity;
	
	@Schema(description = "금액", example = "111400000")
	private BigInteger price;
	
	@Schema(description = "판매일자", example = "2022-12-25")
	private LocalDate saleDate;
}