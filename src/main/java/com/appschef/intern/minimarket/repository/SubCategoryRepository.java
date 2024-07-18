package com.appschef.intern.minimarket.repository;

import com.appschef.intern.minimarket.entity.SubCategories;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategories, Long> {
    //subCategoryCode check
    boolean existsBySubCategoryCode(String subCategoryCode);

    //get sub category detail
    Optional<SubCategories> findBySubCategoryCodeAndIsDeletedFalse(@Param("subCategoryCode") String subCategoryCode);

    //get all sub category
    Page<SubCategories> findByIsDeletedFalse(Pageable pageable);

    //find sub category / cek sub category
    @Query("SELECT p.id FROM SubCategories p WHERE p.subCategoryCode = :subCategoryCode AND p.isDeleted = false")
    Optional<Long> findIdBySubCategoryCodeAndIsDeletedFalse(@Param("subCategoryCode") String subCategoryCode);

    @Modifying
    @Transactional
    @Query("UPDATE SubCategories p SET p.isDeleted = true, p.updatedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
    int softDeleteById(@Param("id") Long id);
}
