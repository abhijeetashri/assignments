package com.thoughtworks.order.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OrderItemDto {

	@JsonProperty(value = "code", required = true)
	private String productCode;

	@JsonProperty(value = "name", required = true)
	private String productName;

	@JsonProperty(value = "quantity", required = true)
	private Long quantity;
}
