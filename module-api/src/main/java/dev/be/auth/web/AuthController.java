package dev.be.auth.web;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.be.auth.service.AuthService;
import dev.be.auth.service.dto.JoinRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "auth", description = "회원 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	
	@PostMapping("/signup")
	@Operation(summary = "signup", description = "회원가입")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
		@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
	})
	public ResponseEntity<?> signUp(@Valid @RequestBody JoinRequest request) {
		return ResponseEntity.ok(authService.signUp(request));
	}
	
}
