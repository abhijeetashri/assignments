package com.thoughtworks.order.item.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "order_item")
@Entity
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "item_id")
	private Long itemId;

	@Column(name = "item_code")
	private String itemCode;

	@Column(name = "item_name")
	private String itemName;

	@Column(name = "item_quantity")
	private Long quantity;

	public OrderItem() {
	}

	public OrderItem(String itemCode, String itemName, Long quantity) {
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.quantity = quantity;
	}
}
