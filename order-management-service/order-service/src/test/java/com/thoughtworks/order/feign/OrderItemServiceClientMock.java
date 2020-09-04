package com.thoughtworks.order.feign;

public class OrderItemServiceClientMock implements OrderItemServiceClient {

	@Override
	public String getItemNameByCode(String itemCode) {
		return "itemA-Test";
	}
}
