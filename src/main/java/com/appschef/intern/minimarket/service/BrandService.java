package com.appschef.intern.minimarket.service;

import com.appschef.intern.minimarket.dto.request.brand.AddBrandRequest;
import com.appschef.intern.minimarket.dto.request.brand.UpdateBrandRequest;
import com.appschef.intern.minimarket.dto.response.brand.BrandDetailResponse;
import com.appschef.intern.minimarket.entity.Brand;
import com.appschef.intern.minimarket.enumMessage.BrandErrorMessage;
import com.appschef.intern.minimarket.mapper.BrandMapper;
import com.appschef.intern.minimarket.repository.BrandRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Transactional
    public BrandDetailResponse addBrand(AddBrandRequest addBrandRequest) {
        Brand brand = BrandMapper.mapToBrand(addBrandRequest);

        //pengecekan kodeBrand duplikat
        if(brandRepository.existsByBrandCode(brand.getBrandCode())){
            throw new ValidationException(BrandErrorMessage.EXIST.getMessage());
        }

        brandRepository.saveAndFlush(brand);
        return BrandMapper.mapToBrandDetailResponse(brand);
    }

    public BrandDetailResponse getBrandByBrandCode(String kodeBrand) {
        Brand brand = brandRepository.findByBrandCodeAndIsDeletedFalse(kodeBrand)
                .orElseThrow(() -> new ValidationException(BrandErrorMessage.NOT_FOUND.getMessage()));

        return BrandMapper.mapToBrandDetailResponse(brand);
    }

    public Page<BrandDetailResponse> getAllBrand(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "brandName"));
        Page<Brand> listBrand = brandRepository.findByIsDeletedFalse(pageable);
        return listBrand.map(BrandMapper::mapToBrandDetailResponse);
    }

    @Transactional
    public BrandDetailResponse updateBrand(String kodeBrand, UpdateBrandRequest newBrand) {
        Brand oldBrand = brandRepository.findByBrandCodeAndIsDeletedFalse(kodeBrand)
                .orElseThrow(() -> new ValidationException(BrandErrorMessage.NOT_FOUND.getMessage()));

        oldBrand.setBrandName(newBrand.getBrandName());
        oldBrand.setUpdatedAt(new Date());

        brandRepository.saveAndFlush(oldBrand);

        return BrandMapper.mapToBrandDetailResponse(oldBrand);
    }

    @Transactional
    public void deleteBrand(String kodeBrand) {
        Long idBrand = brandRepository.findIdByBrandCodeAndIsDeletedFalse(kodeBrand)
                .orElseThrow(() -> new ValidationException(BrandErrorMessage.NOT_FOUND.getMessage()));

        int affectedRows = brandRepository.softDeleteById(idBrand);
        if (affectedRows == 0){
            throw new ValidationException(BrandErrorMessage.GAGAL_HAPUS.getMessage());
        }
    }

}
