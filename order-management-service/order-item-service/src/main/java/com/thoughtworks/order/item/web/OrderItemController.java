package com.thoughtworks.order.item.web;

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

import com.thoughtworks.order.item.dto.OrderItemDto;
import com.thoughtworks.order.item.entity.OrderItem;
import com.thoughtworks.order.item.exception.OrderItemCreationException;
import com.thoughtworks.order.item.service.OrderItemService;
import com.thoughtworks.order.item.validation.OrderItemValidator;

@RestController
@RequestMapping(path = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderItemController {

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private OrderItemValidator validator;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Long> createItem(@RequestBody OrderItemDto itemDto, BindingResult bindingResult) {
		ValidationUtils.invokeValidator(validator, itemDto, bindingResult);
		if (bindingResult.hasErrors()) {
			prepareErrorForFailedOrderItemCreation(bindingResult);
		}
		Long id = orderItemService.createItemOrder(itemDto);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<OrderItemDto>> getAllItems() {
		return new ResponseEntity<>(orderItemService.all(), HttpStatus.OK);
	}

	@GetMapping(path = "/code/{itemCode}")
	public ResponseEntity<String> getItemNameByCode(@PathVariable(name = "itemCode") String itemCode) {
		return new ResponseEntity<>(orderItemService.getItemByCode(itemCode).getItemName(), HttpStatus.OK);
	}

	@GetMapping(path = "/item/{itemName}")
	public ResponseEntity<OrderItem> getOrderByCustomerName(@PathVariable(name = "itemName") String itemName) {
		return new ResponseEntity<>(orderItemService.getItemByItemName(itemName), HttpStatus.OK);
	}

	private void prepareErrorForFailedOrderItemCreation(BindingResult bindingResult) {
		StringBuilder errorMessage = new StringBuilder();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			errorMessage.append(fieldError.getDefaultMessage()).append("\n");
		}
		throw new OrderItemCreationException(errorMessage.toString());
	}
}
