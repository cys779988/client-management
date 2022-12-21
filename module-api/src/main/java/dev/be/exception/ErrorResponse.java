package dev.be.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path.Node;

import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
	private String message;

	private List<FieldError> errors;

	private String code;

	private ErrorResponse(ErrorCode code, List<FieldError> errors) {
		this.message = code.getMessage();
		this.errors = errors;
		this.code = code.getCode();
	}

	private ErrorResponse(ErrorCode code) {
		this.message = code.getMessage();
		this.code = code.getCode();
		this.errors = new ArrayList<>();
	}
	
	public static ErrorResponse of(ErrorCode code, BindingResult bindingResult) {
		return new ErrorResponse(code, FieldError.of(bindingResult));
	}

	public static ErrorResponse of(ErrorCode code) {
		return new ErrorResponse(code);
	}

	public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
		String value = Optional.ofNullable(e.getValue())
			.map(Object::toString)
			.orElse("");

		List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(
			e.getName(), value, e.getErrorCode());
		return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
	}
	
	public static ErrorResponse of(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		
		List<FieldError> errors = constraintViolations.stream().map(i -> {
			Iterator<Node> it = i.getPropertyPath().iterator();
			String name = "";
			while(it.hasNext()) {
				Node node = it.next();
				if(node.getKind() == ElementKind.PROPERTY) {
					name = node.getName();
				}
			}
			return new FieldError(name, i.getInvalidValue(), i.getMessageTemplate());
		}).collect(Collectors.toList());
		
		return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
	}
	
	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class FieldError {
		private String field;
		private Object value;
		private String reason;

		public static List<FieldError> of(String field, Object value, String reason) {
			List<FieldError> fieldErrors = new ArrayList<>();
			fieldErrors.add(new FieldError(field, value, reason));
			return fieldErrors;
		}

		private static List<FieldError> of(BindingResult bindingResult) {
			List<org.springframework.validation.FieldError> fieldErrors = bindingResult
				.getFieldErrors();
			return fieldErrors.stream()
				.map(error -> new FieldError(
					error.getField(),
					error.getRejectedValue() == null ? "" :
						error.getRejectedValue().toString(),
					error.getDefaultMessage()))
				.collect(Collectors.toList());
		}
	}
}