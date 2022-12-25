package dev.be.customer.web;

import java.util.List;

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

import dev.be.customer.service.CustomerService;
import dev.be.customer.service.dto.CustomerRequest;
import dev.be.customer.service.dto.CustomerResponse;
import dev.be.domain.model.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
	@Operation(summary = "regist_customer", description = "고객정보 등록")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "OK")
	})
	public ResponseEntity<Void> regist(@RequestBody CustomerRequest request) {
		customerService.regist(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "update_customer", description = "고객정보 수정")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK")
	})
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody CustomerRequest request) {
		request.setId(id);
		customerService.update(request);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping
	@Operation(summary = "delete_customer", description = "고객정보 삭제")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "OK")
	})
	public ResponseEntity<Void> delete(@Parameter(name = "id", description = "삭제할 고객의 ID", in = ParameterIn.QUERY) Long id) {
		customerService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/basic")
	@Operation(summary = "get_customer_basicinfo", description = "고객 목록 기본정보로 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CustomerResponse.class)))
	})
	public ResponseEntity<List<CustomerResponse>> getCustomersBasicInfo() {
		return ResponseEntity.ok(customerService.getCustomersBasicInfo());
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "get_customer", description = "고객 정보 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CustomerResponse.class)))
	})
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id) {
		return ResponseEntity.ok(customerService.getCustomer(id));
	}
}
