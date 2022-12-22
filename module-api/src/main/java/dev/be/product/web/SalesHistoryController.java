package dev.be.product.web;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.be.product.service.SalesHistoryService;
import dev.be.product.service.dto.SalesRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "sales", description = "제품 판매 관리 API")
@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesHistoryController {
	private final SalesHistoryService salesHistoryService;
	
	@PostMapping
	@Operation(summary = "regist", description = "제품 판매 내역 등록")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "OK"),
		@ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
		@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
	})
	public ResponseEntity<Void> regist(@RequestBody @Valid SalesRequest request) {
		salesHistoryService.regist(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
}
