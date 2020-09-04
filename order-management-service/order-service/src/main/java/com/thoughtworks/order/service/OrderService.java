package com.thoughtworks.order.service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtworks.order.dto.OrderDto;
import com.thoughtworks.order.entity.Order;
import com.thoughtworks.order.exception.IncorrectOrderItemException;
import com.thoughtworks.order.exception.OrderNotFoundException;
import com.thoughtworks.order.feign.OrderItemServiceClient;
import com.thoughtworks.order.repository.OrderRepository;

import feign.FeignException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemServiceClient itemServiceClient;

	public Long createOrder(OrderDto orderDto) {
		validateOrderItems(orderDto.getItems());
		Order newOrder = new Order(orderDto.getCustomerName(), orderDto.getShippingAddress(), LocalDateTime.now(),
				orderDto.getItems(), orderDto.getTotal());
		return orderRepository.save(newOrder).getOrderId();
	}

	private void validateOrderItems(String orderItems) {
		for (String anItem : orderItems.split(";")) {
			try {
				itemServiceClient.getItemNameByCode(anItem);
			} catch (FeignException fe) {
				throw new IncorrectOrderItemException(MessageFormat.format("Incorrect product code {0}", anItem));
			}
		}
	}

	public Order getOrderById(Long orderId) {
		Optional<Order> orderOpt = orderRepository.findById(orderId);
		if (!orderOpt.isPresent()) {
			throw new OrderNotFoundException(MessageFormat.format("Order {0} not found", orderId));
		}

		return orderOpt.get();
	}

	public List<Order> getOrdersByCustomerName(String customerName) {
		List<Order> orders = orderRepository.findByCustomerName(customerName);
		if (orders.isEmpty()) {
			throw new OrderNotFoundException(MessageFormat.format("Order not found for customer {0}", customerName));
		}
		return orders;
	}
}
