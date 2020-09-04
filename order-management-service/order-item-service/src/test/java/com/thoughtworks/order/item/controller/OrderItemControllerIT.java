package com.thoughtworks.order.item.controller;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.order.item.OrderItemServiceApplication;
import com.thoughtworks.order.item.dto.OrderItemDto;
import com.thoughtworks.order.item.entity.OrderItem;
import com.thoughtworks.order.item.repository.OrderItemRepository;
import com.thoughtworks.order.item.service.OrderItemService;
import com.thoughtworks.order.item.validation.OrderItemValidator;

@SpringBootTest(classes = OrderItemServiceApplication.class)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
@TestInstance(Lifecycle.PER_CLASS)
public class OrderItemControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ModelMapper modelMapper;

	@MockBean
	private OrderItemService orderItemService;

	@MockBean
	private OrderItemRepository orderItemRepository;

	@MockBean
	private OrderItemValidator validator;

	private static final String ORDER_ITEM_CODE = "PGST1";
	private static final String ORDER_ITEM_NAME = "ITEM-A";
	OrderItemDto orderItemDto = null;
	OrderItem newItem = null;

	@BeforeAll
	public void init() {
		orderItemDto = new OrderItemDto();
		orderItemDto.setProductCode(ORDER_ITEM_CODE);
		orderItemDto.setProductName(ORDER_ITEM_NAME);
		orderItemDto.setQuantity(Long.valueOf(100));

		newItem = new OrderItem(orderItemDto.getProductCode(), orderItemDto.getProductName(),
				orderItemDto.getQuantity());

		OrderItemService orderItemService = context.getBean(OrderItemService.class);
		ConfigurableApplicationContext configContext = (ConfigurableApplicationContext) context;
		SingletonBeanRegistry beanRegistry = configContext.getBeanFactory();
		((DefaultListableBeanFactory) configContext.getBeanFactory()).destroySingleton("orderItemService");

		Whitebox.setInternalState(orderItemService, "orderItemRepository", orderItemRepository);
		Whitebox.setInternalState(orderItemService, "modelMapper", modelMapper);
		beanRegistry.registerSingleton("orderItemService", orderItemService);
	}

	@Test
	@DisplayName("Create an item successfully")
	@org.junit.jupiter.api.Order(1)
	public void testOrderItemCreate_Success_With200() throws Exception {

		// Prepare
		doCallRealMethod().when(validator).supports(Mockito.any());
		doCallRealMethod().when(validator).validate(Mockito.any(), Mockito.any());
		when(orderItemRepository.save(Mockito.any(OrderItem.class))).thenReturn(newItem);

		// Assign & Assert
		mockMvc.perform(post("/items").characterEncoding(StandardCharsets.UTF_8.displayName())
				.content(objectMapper.writeValueAsString(orderItemDto)).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print()).andExpect(status().isOk());

	}

	@Test
	@DisplayName("Fetch all items")
	@org.junit.jupiter.api.Order(2)
	public void testOrderItem_FetchAll_With200() throws Exception {

		// Prepare
		doCallRealMethod().when(orderItemService).all();
		when(orderItemRepository.findAll()).thenReturn(Collections.singletonList(newItem));

		// Assign
		MvcResult result = mockMvc.perform(get("/items")).andDo(print()).andExpect(status().isOk()).andReturn();

		// Assert
		String response = result.getResponse().getContentAsString();
		Assert.isTrue(!response.isEmpty(), "Response is empty");
		List<OrderItemDto> item = objectMapper.readValue(response, new TypeReference<List<OrderItemDto>>() {
		});
		Assert.isTrue(!item.isEmpty(), "Order list is empty");
		Assert.isTrue(ORDER_ITEM_CODE.equals(item.get(0).getProductCode()), "Wrong item fetched");
	}

	@Test
	@DisplayName("Fetch item by item code")
	@org.junit.jupiter.api.Order(3)
	public void testOrderItem_FetchItemByCode_With200() throws Exception {

		// Prepare
		doCallRealMethod().when(orderItemService).getItemByCode(Mockito.anyString());
		when(orderItemRepository.findByItemCode(Mockito.anyString())).thenReturn(Optional.of(newItem));

		// Assign
		MvcResult result = mockMvc.perform(get("/items/code/" + ORDER_ITEM_CODE)).andDo(print())
				.andExpect(status().isOk()).andReturn();

		// Assert
		String response = result.getResponse().getContentAsString();
		Assert.isTrue(!response.isEmpty(), "Response is empty");
		Assert.isTrue(ORDER_ITEM_NAME.equals(response), "Wrong item fetched");
	}
}
