package dev.be.product.service.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "제품 판매 내역 조회 요청")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesHistorySearchRequest {

	@Schema(description = "고객명", example = "홍길동")
	private String customerName;
	
	@Schema(description = "제품명", example = "에어컨")
	private String productName;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "판매시작일자", example = "2022-10-21")
	private LocalDate salesStartDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "판매종료일자", example = "2022-12-21")
	private LocalDate salesEndDate;
	
}
