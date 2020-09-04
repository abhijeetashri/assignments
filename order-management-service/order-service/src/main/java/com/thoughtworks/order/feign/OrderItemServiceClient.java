package com.thoughtworks.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "item-service", url = "http://localhost:9090")
public interface OrderItemServiceClient {

	@GetMapping(path = "/items/code/{itemCode}")
	String getItemNameByCode(@PathVariable(name = "itemCode") String itemCode);
}
