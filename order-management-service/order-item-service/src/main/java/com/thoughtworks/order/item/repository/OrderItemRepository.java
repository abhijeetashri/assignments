package com.thoughtworks.order.item.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thoughtworks.order.item.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	Optional<OrderItem> findByItemCode(String itemCode);

	Optional<OrderItem> findByItemName(String itemName);
}
