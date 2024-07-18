package com.appschef.intern.minimarket.repository;

import com.appschef.intern.minimarket.entity.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long> {
}
