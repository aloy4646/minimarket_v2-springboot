package com.appschef.intern.minimarket.specification;

import com.appschef.intern.minimarket.entity.Brand;
import com.appschef.intern.minimarket.entity.Product;
import com.appschef.intern.minimarket.entity.SubCategories;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> filterBrand(String brandCode) {
        return (root, query, criteriaBuilder) -> {
            if (brandCode == null || brandCode.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<Product, Brand> brand = root.join("brand");
            return criteriaBuilder.equal(brand.get("brandCode"), brandCode);
        };
    }

    public static Specification<Product> filterSubCategory(String subCategoryCode) {
        return (root, query, criteriaBuilder) -> {
            if (subCategoryCode == null || subCategoryCode.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<Product, SubCategories> subCategories = root.join("subCategories");
            return criteriaBuilder.equal(subCategories.get("subCategoryCode"), subCategoryCode);
        };
    }

    public static Specification<Product> filterCategory(String categoryCode) {
        return (root, query, criteriaBuilder) -> {
            if (categoryCode == null || categoryCode.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<Product, SubCategories> subCategories = root.join("subCategories");
            Join<Product, SubCategories> categories = subCategories.join("categories");
            return criteriaBuilder.equal(categories.get("categoryCode"), categoryCode);
        };
    }

    public static Specification<Product> outOfStock(Boolean outOfStock) {
        return (root, query, criteriaBuilder) -> {
            if (outOfStock == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            if (outOfStock) {
                return criteriaBuilder.equal(root.get("currentStock"), 0);
            } else {
                return criteriaBuilder.greaterThan(root.get("currentStock"), 0);
            }
        };
    }
}
