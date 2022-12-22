package dev.be.product.web;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.be.domain.model.PageResponse;
import dev.be.domain.model.ProductEntity;
import dev.be.product.service.ProductService;
import dev.be.product.service.dto.ProductRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "product", description = "제품관리 API")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;
	
	@PostMapping
	@Operation(summary = "regist", description = "제품정보 등록")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
		@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
	})
	public ResponseEntity<Void> regist(@RequestBody @Valid ProductRequest request) {
		productService.regist(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "update", description = "제품정보 수정")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
		@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
	})
	@Parameter(name = "id", example = "10", description = "수정할 제품 ID", required = true)
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody ProductRequest request) {
		request.setId(id);
		productService.regist(request);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping
	@Operation(summary = "delete", description = "제품정보 삭제")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
		@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
	})
	@Parameter(name = "id", example = "10", description = "삭제할 제품 ID", required = true)
	public ResponseEntity<Void> delete(Long id) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	@Operation(summary = "getProducts", description = "제품정보 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
		@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
	})
	@Parameter(name = "page", example = "2", description = "조회할 페이지 번호")
	public ResponseEntity<PageResponse<ProductEntity>> getProducts(Pageable page) {
		return ResponseEntity.ok(productService.getProducts(page));
	}
}
