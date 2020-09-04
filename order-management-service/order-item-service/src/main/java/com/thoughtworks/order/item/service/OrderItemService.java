package com.thoughtworks.order.item.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtworks.order.item.dto.OrderItemDto;
import com.thoughtworks.order.item.entity.OrderItem;
import com.thoughtworks.order.item.exception.OrderItemNotFoundException;
import com.thoughtworks.order.item.repository.OrderItemRepository;

@Service
public class OrderItemService {

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ModelMapper modelMapper;

	public Long createItemOrder(OrderItemDto itemDto) {
		OrderItem newItem = new OrderItem(itemDto.getProductCode(), itemDto.getProductName(), itemDto.getQuantity());
		return orderItemRepository.save(newItem).getItemId();
	}

	public List<OrderItemDto> all() {
		return orderItemRepository.findAll().stream().map(item -> modelMapper.map(item, OrderItemDto.class))
				.collect(Collectors.toList());
	}

	public OrderItem getItemByCode(String itemCode) {
		Optional<OrderItem> orderOpt = orderItemRepository.findByItemCode(itemCode);
		if (!orderOpt.isPresent()) {
			throw new OrderItemNotFoundException(MessageFormat.format("Item {0} not available", itemCode));
		}

		return orderOpt.get();
	}

	public OrderItem getItemByItemName(String itemName) {
		Optional<OrderItem> orderOpt = orderItemRepository.findByItemName(itemName);
		if (!orderOpt.isPresent()) {
			throw new OrderItemNotFoundException(MessageFormat.format("Item {0} not available", itemName));
		}
		return orderOpt.get();
	}
}
