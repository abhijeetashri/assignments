package com.thoughtworks.order.item.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.thoughtworks.order.item.dto.OrderItemDto;

public class OrderItemValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return OrderItemDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "productCode", "product.code.invalid", "Product code is invalid");
		ValidationUtils.rejectIfEmpty(errors, "productName", "product.name.invalid", "Product name is invalid");
		ValidationUtils.rejectIfEmpty(errors, "quantity", "product.quantity.invalid", "Product quantity is invalid");
	}

}
