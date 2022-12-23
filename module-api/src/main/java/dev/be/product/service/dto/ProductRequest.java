package dev.be.product.service.dto;

import java.math.BigInteger;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import dev.be.domain.model.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "제품 등록 요청")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

	private Long id;

	@NotBlank(message = "제품명은 필수값입니다.")
	@Schema(description = "제품명", example = "좋은제품")
	private String name;
	
	@NotNull(message = "재고 수량은 필수값입니다.")
	@Min(value = 1, message = "재고 수량을 1개 이상으로 입력해주세요.")
	@Schema(description = "제품재고", example = "10")
	private Long quantity;
	
	@NotNull(message = "금액은 필수값입니다.")
	@Min(value = 1, message = "금액은 1원 이상으로 입력해주세요.")
	@Schema(description = "제품금액", example = "10000")
	private BigInteger price;
	
	public ProductEntity toEntity() {
		return ProductEntity.builder()
					.id(id)
					.name(name)
					.quantity(quantity)
					.price(price)
					.build();
	}
}
