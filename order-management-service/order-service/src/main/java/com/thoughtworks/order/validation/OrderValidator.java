package com.thoughtworks.order.validation;

import java.math.BigDecimal;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.thoughtworks.order.dto.OrderDto;

public class OrderValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return OrderDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "customerName", "customer.name.invalid", "Customer name is invalid");
		ValidationUtils.rejectIfEmpty(errors, "shippingAddress", "shipping.address.invalid",
				"Shipping address is invalid");
		ValidationUtils.rejectIfEmpty(errors, "items", "items.invalid",
				"There must be atleast one item for order to be placed");

		OrderDto orderDto = (OrderDto) target;
		if (orderDto.getTotal() == null || orderDto.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
			errors.rejectValue("total", "total.invalid", "Order price must be greater than Zero");
		}
	}

}
