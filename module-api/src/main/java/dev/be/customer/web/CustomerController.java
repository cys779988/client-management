package dev.be.customer.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.be.customer.service.CustomerService;
import dev.be.customer.service.dto.CustomerRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "customer", description = "고객관리 API")
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
	private final CustomerService customerService;
	
	@PostMapping
	@Operation(summary = "regist", description = "고객정보 등록")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
		@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
	})
	public ResponseEntity<?> regist(@RequestBody CustomerRequest request) {
		customerService.regist(request);
		return ResponseEntity.ok("");
	}
}
