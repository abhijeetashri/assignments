package com.thoughtworks.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "orders")
@Entity
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "shipping_address")
	private String shippingAddress;

	@Column(name = "order_date")
	private LocalDateTime orderDate;

	@Column(name = "items")
	private String items;

	@Column(name = "total")
	private BigDecimal total;

	public Order() {
	}

	public Order(String customerName, String shippingAddress, LocalDateTime orderDate, String items, BigDecimal total) {
		this.customerName = customerName;
		this.shippingAddress = shippingAddress;
		this.orderDate = orderDate;
		this.items = items;
		this.total = total;
	}
}
