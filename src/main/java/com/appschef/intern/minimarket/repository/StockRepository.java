package com.appschef.intern.minimarket.repository;

import com.appschef.intern.minimarket.entity.LogStock;
import com.appschef.intern.minimarket.projection.StockReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;

public interface StockRepository extends JpaRepository<LogStock, Long> {

    @Query(value = "SELECT SUM( " +
            "CASE WHEN type_stock = 'SALE' THEN -(quantity) ELSE 0 END " +
            ") AS totalSales, " +
            "SUM( " +
            "CASE WHEN type_stock = 'EXPIRATION' THEN -(quantity) ELSE 0 END " +
            ") AS totalExpired, " +
            "SUM( " +
            "CASE WHEN type_stock = 'ADDITION' THEN quantity ELSE 0 END " +
            ") AS totalAdditions " +
            "FROM log_stock " +
            "WHERE DATE(log_stock_date) = :date " +
            "AND product_id = :productId " +
            "GROUP BY product_id", nativeQuery = true)
    StockReportProjection getStockReport (@Param("date") LocalDate date, @Param("productId") Long productId);
}
