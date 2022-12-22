package dev.be.product.web;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.be.product.service.ProductService;
import dev.be.product.service.dto.ProductRequest;
import io.swagger.v3.oas.annotations.Operation;
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
	public ResponseEntity<Void> delete(Long id) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
