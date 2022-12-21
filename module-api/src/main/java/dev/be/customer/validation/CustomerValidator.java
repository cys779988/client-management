package dev.be.customer.validation;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.util.CollectionUtils;

import dev.be.customer.service.dto.CustomerRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomerValidator implements ConstraintValidator<CustomerConstarint, CustomerRequest> {
	private Validator validator;
	
	@Override
	public boolean isValid(CustomerRequest value, ConstraintValidatorContext context) {
		Class<?> [] groups = {};
		
		switch (value.getType()) {
		case FOREIGN_CORPORATION:
			groups = new Class []{CustomerMarker.class, RepresentiveMarker.class};
			break;
		case KOREAN_CORPORATION:
			groups = new Class []{RepresentiveMarker.class};
			break;
		case FOREIGN:
			groups = new Class []{CustomerMarker.class};
			break;
		case KOREAN: 
			break;
		}
		
		if(groups.length > 0 && !this.validate(context, value, groups)) {
			return false;
		}
		
		return true;
	}
	
	private <T> boolean validate(ConstraintValidatorContext context, T value, Class<?>... groups) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(value, groups);
		if(!CollectionUtils.isEmpty(constraintViolations)) {
			context.disableDefaultConstraintViolation();
			constraintViolations.stream()
								.forEach(cv -> {
									context.buildConstraintViolationWithTemplate(cv.getMessageTemplate())
									.addPropertyNode(cv.getPropertyPath().toString())
									.addConstraintViolation();
								});
			return false;
		}
		return true;
	}
}
