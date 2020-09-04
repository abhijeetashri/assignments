package com.thoughtworks.order.controller;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.order.dto.OrderDto;
import com.thoughtworks.order.entity.Order;
import com.thoughtworks.order.exception.ErrorDetails;
import com.thoughtworks.order.feign.OrderItemServiceClientMock;
import com.thoughtworks.order.repository.OrderRepository;
import com.thoughtworks.order.service.OrderService;
import com.thoughtworks.order.validation.OrderValidator;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
@TestInstance(Lifecycle.PER_CLASS)
public class OrderControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderValidator validator;

	private static String ORDER_ID = null;
	private static String ORDER_CUSTOMER_NAME = null;
	private static final String ERROR_MESSAGE_DELIMITER = " ";
	private static final String RANDOM_ORDER_NUMBER_CHAR_REMOVER = ",";

	@BeforeAll
	public void init() throws NoSuchFieldException, SecurityException {
		OrderService orderService = context.getBean(OrderService.class);
		ConfigurableApplicationContext configContext = (ConfigurableApplicationContext) context;
		SingletonBeanRegistry beanRegistry = configContext.getBeanFactory();
		((DefaultListableBeanFactory) configContext.getBeanFactory()).destroySingleton("orderService");

		OrderItemServiceClientMock itemServiceClient = new OrderItemServiceClientMock();
		Whitebox.setInternalState(orderService, "itemServiceClient", itemServiceClient);
		beanRegistry.registerSingleton("orderService", orderService);
	}

	@Test
	@org.junit.jupiter.api.Order(1)
	public void testOrderCreate_Success_With200() throws Exception {

		// Prepare
		OrderDto orderDto = new OrderDto();
		orderDto.setCustomerName("abhijeet");
		orderDto.setShippingAddress("noida");
		orderDto.setTotal(BigDecimal.valueOf(10000));
		orderDto.setItems("PTGS1");
		Order newOrder = new Order(orderDto.getCustomerName(), orderDto.getShippingAddress(), LocalDateTime.now(),
				orderDto.getItems(), orderDto.getTotal());

		doCallRealMethod().when(validator).supports(Mockito.any());
		doCallRealMethod().when(validator).validate(Mockito.any(), Mockito.any());
		when(orderRepository.save(Mockito.any(Order.class))).thenReturn(newOrder);

		// Assign & Assert
		MvcResult result = mockMvc.perform(post("/orders").content(objectMapper.writeValueAsString(orderDto))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andReturn();

		ORDER_ID = result.getResponse().getContentAsString();
	}

	@Test
	@org.junit.jupiter.api.Order(2)
	public void testGetOrderById_With200() throws Exception {

		// Prepare

		// Assign
		MvcResult result = mockMvc.perform(get("/orders/" + ORDER_ID)).andExpect(status().isOk()).andReturn();

		// Assert
		String response = result.getResponse().getContentAsString();
		Assert.isTrue(!response.isEmpty(), "Response is empty");
		Order order = objectMapper.readValue(response, Order.class);
		Assert.notNull(order, "Order cannot be NULL");
		ORDER_CUSTOMER_NAME = order.getCustomerName();
		Assert.isTrue("abhijeet".equals(ORDER_CUSTOMER_NAME), "Wrong Order fetched");
	}

	@Test
	@org.junit.jupiter.api.Order(3)
	public void testGetOrderByCustomerName_With200() throws Exception {

		// Prepare

		// Assign
		MvcResult result = mockMvc.perform(get("/orders/customer/" + ORDER_CUSTOMER_NAME)).andExpect(status().isOk())
				.andReturn();

		// Assert
		String response = result.getResponse().getContentAsString();
		Assert.isTrue(!response.isEmpty(), "Response is empty");
		List<Order> order = objectMapper.readValue(response, new TypeReference<List<Order>>() {
		});
		Assert.isTrue(!order.isEmpty(), "Order list is empty");
		Assert.isTrue(ORDER_CUSTOMER_NAME.equals(order.get(0).getCustomerName()), "Wrong Order fetched");
	}

	@Test
	@org.junit.jupiter.api.Order(4)
	public void testGetOrderByCustomerName_NotFound() throws Exception {

		// Prepare
		String randomCustomerName = "12345bsdhbashdb";

		// Assign
		MvcResult result = mockMvc.perform(get("/orders/customer/" + randomCustomerName))
				.andExpect(status().isNotFound()).andReturn();

		// Assert
		String response = result.getResponse().getContentAsString();
		Assert.isTrue(!response.isEmpty(), "Response is empty");
		ErrorDetails error = objectMapper.readValue(response, ErrorDetails.class);
		Assertions.assertTrue("Order not found for customer 12345bsdhbashdb".equals(error.getErrorMessage()),
				"Wrong error message");
	}

	@Test
	@org.junit.jupiter.api.Order(5)
	public void testGetOrderByOrderId_NotFound() throws Exception {

		// Prepare
		String randomOrderId = String.valueOf(new Random().nextLong());

		// Assign
		MvcResult result = mockMvc.perform(get("/orders/" + randomOrderId)).andExpect(status().isNotFound())
				.andReturn();

		// Assert
		String response = result.getResponse().getContentAsString();
		Assert.isTrue(!response.isEmpty(), "Response is empty");
		ErrorDetails error = objectMapper.readValue(response, ErrorDetails.class);
		String errorMsg = cleanNumber(error.getErrorMessage());
		Assertions.assertTrue(MessageFormat.format("Order {0} not found", randomOrderId).equals(errorMsg),
				"Wrong error message");
	}

	private String cleanNumber(String errorMessage) {
		StringBuilder errorMsg = new StringBuilder();
		for (String aWord : StringUtils.split(errorMessage, ERROR_MESSAGE_DELIMITER)) {
			try {
				String cleanedText = StringUtils.remove(aWord, RANDOM_ORDER_NUMBER_CHAR_REMOVER);

				// checks if its valid random number generator
				Long.parseLong(cleanedText);
				errorMsg.append(cleanedText).append(ERROR_MESSAGE_DELIMITER);
			} catch (NumberFormatException nfe) {

				// Exception, if the word is non-numeric
				errorMsg.append(aWord).append(ERROR_MESSAGE_DELIMITER);
			}
		}

		// trim string and return
		return StringUtils.trim(errorMsg.toString());
	}
}
