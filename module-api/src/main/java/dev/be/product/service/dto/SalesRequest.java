package dev.be.product.service.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesRequest {
	
	@NotNull(message = "고객을 선택해주세요.")
	private Long customerId;
	
	@NotNull(message = "제품을 선택해주세요.")
	private Long productId;
	
	@NotNull(message = "제품수량은 필수값입니다.")
	private Long quantity;
	
	@NotNull(message = "판매일자는 필수값입니다.")
	private LocalDate saleDate;
}
