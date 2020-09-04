package com.thoughtworks.order.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OrderDto {

	@JsonProperty(value = "customer_name")
	private String customerName;

	@JsonProperty(value = "shipping_address")
	private String shippingAddress;

	@JsonProperty(value = "total")
	private BigDecimal total;

	@JsonProperty(value = "items")
	private String items;
}
