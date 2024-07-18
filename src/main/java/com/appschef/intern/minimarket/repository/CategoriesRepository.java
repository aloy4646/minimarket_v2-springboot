package com.appschef.intern.minimarket.repository;

import com.appschef.intern.minimarket.entity.Categories;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    //categoryCode check
    boolean existsByCategoryCode(String categoryCode);

    //get category detail
    Optional<Categories> findByCategoryCodeAndIsDeletedFalse(@Param("categoryCode") String categoryCode);

    //get all categories
    Page<Categories> findByIsDeletedFalse(Pageable pageable);

    //find category / cek category
    @Query("SELECT p.id FROM Categories p WHERE p.categoryCode = :categoryCode AND p.isDeleted = false")
    Optional<Long> findIdByCategoryCodeAndIsDeletedFalse(@Param("categoryCode") String categoryCode);

    @Modifying
    @Transactional
    @Query("UPDATE Categories p SET p.isDeleted = true, p.updatedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
    int softDeleteById(@Param("id") Long id);
}
