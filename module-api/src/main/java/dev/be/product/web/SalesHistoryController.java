package dev.be.product.web;

import java.io.IOException;
import java.net.URLEncoder;

import javax.validation.Valid;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.be.domain.model.PageResponse;
import dev.be.domain.model.SalesHistoryEntity;
import dev.be.product.service.SalesHistoryService;
import dev.be.product.service.dto.SalesHistorySearchRequest;
import dev.be.product.service.dto.SalesRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
		@ApiResponse(responseCode = "201", description = "OK")
	})
	public ResponseEntity<Void> regist(@RequestBody @Valid SalesRequest request) {
		salesHistoryService.regist(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping
	@Operation(summary = "getSalesHistory", description = "제품 판매 내역 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PageResponse.class)))
	})
	public ResponseEntity<PageResponse<SalesHistoryEntity>> getSalesHistory(@Parameter(name = "page", example = "2", description = "조회할 페이지 번호", in = ParameterIn.QUERY)
																			Pageable page, SalesHistorySearchRequest request) {
		return ResponseEntity.ok(salesHistoryService.getSalesHistory(page, request));
	}
	
	@GetMapping("/excel")
	@Operation(summary = "getSalesHistoryExcelFile", description = "제품 판매 내역 엑셀다운로드")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK")
	})
	public ResponseEntity<InputStreamResource> getSalesHistoryExcelFile(@Parameter(name = "page", example = "2", description = "엑셀다운로드 할 페이지 번호", in = ParameterIn.QUERY)
																		Pageable page, SalesHistorySearchRequest request) throws IOException {
		
		InputStreamResource resource = salesHistoryService.getSalesHistoryExcelFile(page, request);
		String filename = URLEncoder.encode("재품판매내역.xlsx", "UTF-8");
		
		return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + "")
						.contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
						.body(resource);
	}
}
