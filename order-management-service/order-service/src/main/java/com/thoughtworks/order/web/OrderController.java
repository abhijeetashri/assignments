package com.thoughtworks.order.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thoughtworks.order.dto.OrderDto;
import com.thoughtworks.order.entity.Order;
import com.thoughtworks.order.exception.OrderCreationException;
import com.thoughtworks.order.service.OrderService;
import com.thoughtworks.order.validation.OrderValidator;

@RestController
@RequestMapping(path = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderValidator validator;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderDto, BindingResult bindingResult) {
		ValidationUtils.invokeValidator(validator, orderDto, bindingResult);
		if (bindingResult.hasErrors()) {
			prepareErrorForFailedOrderCreation(bindingResult);
		}
		Long id = orderService.createOrder(orderDto);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}

	@GetMapping(path = "/{orderId}")
	public ResponseEntity<Order> getOrderById(@PathVariable(name = "orderId") Long orderId) {
		return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);
	}

	@GetMapping(path = "/customer/{customerName}")
	public ResponseEntity<List<Order>> getOrderByCustomerName(
			@PathVariable(name = "customerName") String customerName) {
		return new ResponseEntity<>(orderService.getOrdersByCustomerName(customerName), HttpStatus.OK);
	}

	private void prepareErrorForFailedOrderCreation(BindingResult bindingResult) {
		StringBuilder errorMessage = new StringBuilder();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			errorMessage.append(fieldError.getDefaultMessage()).append("\n");
		}
		throw new OrderCreationException(errorMessage.toString());
	}
}
