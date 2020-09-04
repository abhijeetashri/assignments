package com.thoughtworks.order.item.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thoughtworks.order.item.dto.OrderItemDto;
import com.thoughtworks.order.item.entity.OrderItem;
import com.thoughtworks.order.item.validation.OrderItemValidator;

@Configuration
public class OrderItemServiceConfiguration {

	@Bean
	public OrderItemValidator validator() {
		return new OrderItemValidator();
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();

		Converter<OrderItem, OrderItemDto> entityToDtoConverter = new Converter<OrderItem, OrderItemDto>() {

			@Override
			public OrderItemDto convert(MappingContext<OrderItem, OrderItemDto> context) {
				if (context.getSource() == null)
					return null;
				OrderItem source = context.getSource();
				OrderItemDto dest = new OrderItemDto();
				dest.setProductCode(source.getItemCode());
				dest.setProductName(source.getItemName());
				dest.setQuantity(source.getQuantity());
				return dest;
			}
		};

		mapper.addConverter(entityToDtoConverter);

		Converter<OrderItemDto, OrderItem> dtoToEntityConverter = new Converter<OrderItemDto, OrderItem>() {

			@Override
			public OrderItem convert(MappingContext<OrderItemDto, OrderItem> context) {
				if (context.getSource() == null)
					return null;
				OrderItemDto source = context.getSource();
				OrderItem dest = new OrderItem();
				dest.setItemCode(source.getProductCode());
				dest.setItemName(source.getProductName());
				dest.setQuantity(source.getQuantity());
				return dest;
			}
		};

		mapper.addConverter(dtoToEntityConverter);
		return mapper;
	}
}
