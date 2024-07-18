package com.appschef.intern.minimarket.repository;

import com.appschef.intern.minimarket.entity.Members;
import com.appschef.intern.minimarket.projection.TopMemberProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, Long> {
    //get last member number
    @Query(value = "SELECT member_number FROM members " +
            "WHERE member_number LIKE :memberNumberFormat " +
            "ORDER BY member_number DESC " +
            "LIMIT 1", nativeQuery = true)
    String getLastMemberNumber(@Param("memberNumberFormat") String memberNumberFormat);

    //get member detail
    Optional<Members> findByMemberNumberAndIsDeletedFalse(@Param("memberNumber") String memberNumber);

    //get all member
    Page<Members> findByIsDeletedFalse(Pageable pageable);

    //find member / cek member
    @Query("SELECT m.id FROM Members m WHERE m.memberNumber = :memberNumber AND m.isDeleted = false")
    Optional<Long> findIdByMemberNumberAndIsDeletedFalse(@Param("memberNumber") String memberNumber);

    //soft is_deleted member
    @Modifying
    @Transactional
    @Query("UPDATE Members m SET m.isDeleted = true, m.updatedAt = :updatedAt WHERE m.id = :id")
    int softDeleteById(@Param("id") Long id, @Param("updatedAt") Date updatedAt);

    @Query(value = "SELECT a.member_number as memberNumber, a.full_name as fullName, " +
            "COALESCE(SUM(c.product_final_price * c.quantity), 0) AS totalPurchase " +
            "FROM members a " +
            "LEFT JOIN orders b ON b.members_id = a.id " +
            "LEFT JOIN orders_detail c ON c.orders_id = b.id " +
            "GROUP BY a.member_number, a.full_name " +
            "ORDER BY totalPurchase DESC " +
            "LIMIT 3 ", nativeQuery = true)
    List<TopMemberProjection> getTopMember();
}
