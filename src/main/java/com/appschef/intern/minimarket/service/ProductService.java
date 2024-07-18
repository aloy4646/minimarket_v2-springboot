package com.appschef.intern.minimarket.service;

import com.appschef.intern.minimarket.dto.request.product.AddProductRequest;
import com.appschef.intern.minimarket.dto.request.product.UpdateProductRequest;
import com.appschef.intern.minimarket.dto.response.product.ProductDetailResponse;
import com.appschef.intern.minimarket.entity.Brand;
import com.appschef.intern.minimarket.entity.Product;
import com.appschef.intern.minimarket.entity.SubCategories;
import com.appschef.intern.minimarket.enumMessage.BrandErrorMessage;
import com.appschef.intern.minimarket.enumMessage.CategoriesErrorMessage;
import com.appschef.intern.minimarket.enumMessage.ProductErrorMessage;
import com.appschef.intern.minimarket.enumMessage.SubCategoryErrorMessage;
import com.appschef.intern.minimarket.mapper.ProductMapper;
import com.appschef.intern.minimarket.repository.BrandRepository;
import com.appschef.intern.minimarket.repository.CategoriesRepository;
import com.appschef.intern.minimarket.repository.ProductRepository;
import com.appschef.intern.minimarket.repository.SubCategoryRepository;
import com.appschef.intern.minimarket.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Transactional
    public ProductDetailResponse addProduct(AddProductRequest addProductRequest) {
        Product product = ProductMapper.mapToProduct(addProductRequest);

        //productCode duplication check
        if(productRepository.existsByProductCode(product.getProductCode())){
            throw new ValidationException(ProductErrorMessage.EXIST.getMessage());
        }

        Brand brand = brandRepository.findByBrandCodeAndIsDeletedFalse(addProductRequest.getBrandCode())
                .orElseThrow(() -> new ValidationException(BrandErrorMessage.NOT_FOUND.getMessage()));

        SubCategories subCategories = subCategoryRepository.findBySubCategoryCodeAndIsDeletedFalse(addProductRequest.getSubCategoryCode())
                .orElseThrow(() -> new ValidationException(SubCategoryErrorMessage.NOT_FOUND.getMessage()));

        product.setBrand(brand);
        product.setSubCategories(subCategories);
        productRepository.saveAndFlush(product);
        return ProductMapper.mapToProductDetailResponse(product);
    }

    public ProductDetailResponse getProductByProductCode(String productCode) {
        Product product = productRepository.findByProductCodeAndIsDeletedFalse(productCode)
                .orElseThrow(() -> new ValidationException(ProductErrorMessage.NOT_FOUND.getMessage()));

        return ProductMapper.mapToProductDetailResponse(product);
    }

    public Page<ProductDetailResponse> getAllProduct(
            Pageable pageable,
            String brandCode,
            String subCategoryCode,
            String categoryCode,
            Boolean outOfStock) {

        if(brandCode != null && !brandCode.isEmpty() && !brandRepository.existsByBrandCode(brandCode)){
            throw new ValidationException(BrandErrorMessage.NOT_FOUND.getMessage());
        }

        if(subCategoryCode != null && !subCategoryCode.isEmpty() && !subCategoryRepository.existsBySubCategoryCode(subCategoryCode)){
            throw new ValidationException(SubCategoryErrorMessage.NOT_FOUND.getMessage());
        }

        if(categoryCode != null && !categoryCode.isEmpty() && !categoriesRepository.existsByCategoryCode(categoryCode)){
            throw new ValidationException(CategoriesErrorMessage.NOT_FOUND.getMessage());
        }

        pageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "productName"));

        Specification<Product> specification = Specification.where(
                        ProductSpecification.filterBrand(brandCode))
                .and(ProductSpecification.filterSubCategory(subCategoryCode))
                .and(ProductSpecification.filterCategory(categoryCode))
                .and(ProductSpecification.outOfStock(outOfStock))
                .and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDeleted")));

        Page<Product> listProduct = productRepository.findAll(specification, pageable);
        return listProduct.map(ProductMapper::mapToProductDetailResponse);
    }

    @Transactional
    public ProductDetailResponse updateProduct(String productCode, UpdateProductRequest newProduct) {
        Product oldProduct = productRepository.findByProductCodeAndIsDeletedFalse(productCode)
                .orElseThrow(() -> new ValidationException(ProductErrorMessage.NOT_FOUND.getMessage()));

        oldProduct.setProductName(newProduct.getProductName());
        oldProduct.setPrice(newProduct.getPrice());
        oldProduct.setUpdatedAt(new Date());

        productRepository.saveAndFlush(oldProduct);

        return ProductMapper.mapToProductDetailResponse(oldProduct);
    }

    @Transactional
    public void deleteProduct(String productCode) {
        Long idProduct = productRepository.findIdByProductCodeAndIsDeletedFalse(productCode)
                .orElseThrow(() -> new ValidationException(ProductErrorMessage.NOT_FOUND.getMessage()));

        int affectedRows = productRepository.softDeleteById(idProduct);
        if (affectedRows == 0){
            throw new ValidationException(ProductErrorMessage.GAGAL_HAPUS.getMessage());
        }
    }


}