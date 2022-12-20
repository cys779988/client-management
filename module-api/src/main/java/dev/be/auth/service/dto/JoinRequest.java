package dev.be.auth.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import dev.be.domain.model.Role;
import dev.be.domain.model.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinRequest {

	@NotBlank(message = "이메일은 필수값입니다.")
	@Email(message = "이메일 형식에 맞지 않습니다.")
	@Schema(description = "이메일", example = "hong1@helpme.com")
	private String email;

	@NotBlank(message = "비밀번호는 필수값입니다.")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
	@Schema(description = "비밀번호", example = "Helpme12@@")
	private String password;

	@NotBlank(message = "이름은 필수값입니다.")
	@Schema(description = "이름", example = "홍길동")
	private String name;
	
	public UserEntity toEntity(Role role) {
		return UserEntity.builder()
				.name(name)
				.password(password)
				.email(email)
				.role(role)
				.build();
	}
}
