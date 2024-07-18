package com.appschef.intern.minimarket.repository;

import com.appschef.intern.minimarket.entity.Brand;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    //brandCode check
    boolean existsByBrandCode(String brandCode);

    //get brand detail
    Optional<Brand> findByBrandCodeAndIsDeletedFalse(@Param("brandCode") String brandCode);

    //get all brand
    Page<Brand> findByIsDeletedFalse(Pageable pageable);

    //find brand / cek brand
    @Query("SELECT p.id FROM Brand p WHERE p.brandCode = :brandCode AND p.isDeleted = false")
    Optional<Long> findIdByBrandCodeAndIsDeletedFalse(@Param("brandCode") String brandCode);

    @Modifying
    @Transactional
    @Query("UPDATE Brand p SET p.isDeleted = true, p.updatedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
    int softDeleteById(@Param("id") Long id);
}
