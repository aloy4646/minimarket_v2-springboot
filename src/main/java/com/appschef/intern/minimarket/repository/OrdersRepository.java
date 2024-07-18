package com.appschef.intern.minimarket.repository;

import com.appschef.intern.minimarket.entity.Orders;
import com.appschef.intern.minimarket.projection.DailySalesProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    //get last receipt number
    @Query(value = "SELECT receipt_number FROM orders " +
            "WHERE receipt_number LIKE :receiptNumberFormat " +
            "ORDER BY receipt_number DESC " +
            "LIMIT 1", nativeQuery = true)
    String getLastReceiptNumber(@Param("receiptNumberFormat") String receiptNumberFormat);


    @Query(value = "SELECT DATE(a.order_date) AS orderDate, " +
            "SUM(b.product_price * b.quantity) AS totalGrossSales, " +
            "SUM(b.product_final_price * b.quantity) AS totalNetSales " +
            "FROM orders a " +
            "JOIN orders_detail b ON b.orders_id = a.id " +
            "WHERE DATE(a.order_date) = :date " +
            "GROUP BY DATE(a.order_date)", nativeQuery = true)
    DailySalesProjection getDailySalesReport(@Param("date") Date date);
}
