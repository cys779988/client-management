package dev.be.product.service.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "제품 판매 내역 등록 요청")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesRequest {
	
	@NotNull(message = "고객을 선택해주세요.")
	@Schema(description = "고객ID", example = "1")
	private Long customerId;
	
	@NotNull(message = "제품을 선택해주세요.")
	@Schema(description = "제품ID", example = "1")
	private Long productId;
	
	@NotNull(message = "제품수량은 필수값입니다.")
	@Schema(description = "제품수량", example = "10")
	private Long quantity;
	
	@NotNull(message = "판매일자는 필수값입니다.")
	@Schema(description = "판매일자", example = "2022-12-23")
	private LocalDate saleDate;
}
