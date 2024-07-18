package com.appschef.intern.minimarket.repository;

import com.appschef.intern.minimarket.entity.Product;
import com.appschef.intern.minimarket.projection.TopProductProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    //productCode check
    boolean existsByProductCode(String productCode);

    //get produk detail
    Optional<Product> findByProductCodeAndIsDeletedFalse(@Param("productCode") String productCode);

//    //get all produk
//    Page<Product> findByIsDeletedFalse(Pageable pageable);

    //find produk / cek produk
    @Query("SELECT p.id FROM Product p WHERE p.productCode = :productCode AND p.isDeleted = false")
    Optional<Long> findIdByProductCodeAndIsDeletedFalse(@Param("productCode") String productCode);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.isDeleted = true, p.updatedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
    int softDeleteById(@Param("id") Long id);

    @Query(value = "SELECT a.product_code as productCode, " +
            "SUM(b.quantity) AS numberOfPurchases " +
            "FROM product a " +
            "JOIN orders_detail b ON b.product_id = a.id " +
            "JOIN orders c ON c.id = b.orders_id " +
            "JOIN members d ON d.id = c.members_id " +
            "WHERE d.member_number = :memberNumber " +
            "GROUP BY a.product_code " +
            "ORDER BY numberOfPurchases DESC " +
            "LIMIT 3;", nativeQuery = true)
    List<TopProductProjection> getTopProductMember(@Param("memberNumber") String memberNumber);

    @Query(value = "SELECT a.product_code, " +
            "COALESCE(SUM(CASE WHEN DATE(c.order_date) BETWEEN :startDate AND :endDate THEN b.quantity ELSE 0 END), 0) AS numberOfPurchases " +
            "FROM product a " +
            "LEFT JOIN orders_detail b ON b.product_id = a.id " +
            "LEFT JOIN orders c ON c.id = b.orders_id " +
            "GROUP BY a.product_code " +
            "ORDER BY numberOfPurchases DESC " +
            "LIMIT 3", nativeQuery = true)
    List<TopProductProjection> getTopProduct(@Param("startDate") Date startDate , @Param("endDate") Date endDate);

    @Query("SELECT p.productName FROM Product p WHERE p.productCode = :productCode")
    String findProductNameByProductCode(@Param("productCode") String productCode);
}
