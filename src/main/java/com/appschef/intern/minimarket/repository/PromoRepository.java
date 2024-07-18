package com.appschef.intern.minimarket.repository;

import com.appschef.intern.minimarket.entity.Product;
import com.appschef.intern.minimarket.entity.Promo;
import com.appschef.intern.minimarket.projection.TopMemberProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PromoRepository extends JpaRepository<Promo, Long> {
    //promoCode check
    boolean existsByPromoCode(String promoCode);

    //get promo detail
    Optional<Promo> findByPromoCode(@Param("promoCode") String promoCode);

    //get all promo
    Page<Promo> findAll(Pageable pageable);

    //find promo / cek promo
    Optional<Long> findIdByPromoCode(@Param("promoCode") String promoCode);

    @Query("SELECT promo " +
            "FROM Promo promo JOIN promo.product product " +
            "WHERE :date BETWEEN promo.startDate AND promo.endDate " +
            "AND product = :product ")
    List<Promo> findValidPromoByProduct(@Param("date") Date date, @Param("product") Product product);

    @Query(value = "SELECT COALESCE(COUNT(c.id),0) AS numberOfPurchases " +
            "FROM orders_promo a " +
            "JOIN orders_detail b ON a.orders_detail_id = b.id " +
            "JOIN orders c ON b.orders_id = c.id " +
            "WHERE a.promo_code = :promoCode ", nativeQuery = true)
    Long getNumberOfPromoUsages (@Param("promoCode") String promoCode);

    @Query(value = "SELECT d.member_number as memberNumber, d.full_name as fullName, " +
            "COUNT(c.id) AS totalPurchase " +
            "FROM orders_promo a " +
            "JOIN orders_detail b ON a.orders_detail_id = b.id " +
            "JOIN orders c ON b.orders_id = c.id " +
            "JOIN members d ON c.members_id = d.id " +
            "WHERE a.promo_code = :promoCode AND c.members_id IS NOT NULL " +
            "GROUP BY d.member_number, d.full_name", nativeQuery = true)
    List<TopMemberProjection> topPromoUsingMembers(@Param("promoCode") String promoCode);
}
