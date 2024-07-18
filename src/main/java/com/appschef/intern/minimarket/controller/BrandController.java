package com.appschef.intern.minimarket.controller;

import com.appschef.intern.minimarket.dto.request.brand.AddBrandRequest;
import com.appschef.intern.minimarket.dto.request.brand.UpdateBrandRequest;
import com.appschef.intern.minimarket.dto.response.WebResponse;
import com.appschef.intern.minimarket.dto.response.brand.BrandDetailResponse;
import com.appschef.intern.minimarket.service.BrandService;
import com.appschef.intern.minimarket.service.ValidationService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @Autowired
    private ValidationService validationService;

    @PostMapping("/")
    public ResponseEntity<WebResponse<BrandDetailResponse>> createBrand(@Valid @RequestBody AddBrandRequest addBrandRequest, BindingResult bindingResult){
        validationService.validate(bindingResult);

        try{
            BrandDetailResponse savedBrand = brandService.addBrand(addBrandRequest);
            WebResponse<BrandDetailResponse> response = WebResponse.<BrandDetailResponse>builder()
                    .status("success")
                    .data(savedBrand)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<BrandDetailResponse> errorResponse = WebResponse.<BrandDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{brandCode}")
    public ResponseEntity<WebResponse<BrandDetailResponse> >getBrand(@PathVariable("brandCode") String brandCode){
        try{
            BrandDetailResponse promoResponseDTO = brandService.getBrandByBrandCode(brandCode);
            WebResponse<BrandDetailResponse> response =  WebResponse.<BrandDetailResponse>builder()
                    .status("success")
                    .data(promoResponseDTO)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<BrandDetailResponse> errorResponse = WebResponse.<BrandDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<WebResponse<Page<BrandDetailResponse>>> getAllBrand(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try{
            Page<BrandDetailResponse> listBrand = brandService.getAllBrand(PageRequest.of(page, size));
            WebResponse<Page<BrandDetailResponse>> response =  WebResponse.<Page<BrandDetailResponse>>builder()
                    .status("success")
                    .data(listBrand)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e) {
            WebResponse<Page<BrandDetailResponse>> errorResponse = WebResponse.<Page<BrandDetailResponse>>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{brandCode}")
    public ResponseEntity<WebResponse<BrandDetailResponse>> updateBrand(@PathVariable("brandCode") String brandCode,
                                                                          @Valid @RequestBody UpdateBrandRequest updateBrandRequest,
                                                                          BindingResult bindingResult){

        validationService.validate(bindingResult);

        try{
            BrandDetailResponse promoResponseDTO = brandService.updateBrand(brandCode, updateBrandRequest);
            WebResponse<BrandDetailResponse> response = WebResponse.<BrandDetailResponse>builder()
                    .status("success")
                    .data(promoResponseDTO)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<BrandDetailResponse> errorResponse = WebResponse.<BrandDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{brandCode}")
    public ResponseEntity<WebResponse<String>> deleteMember(@PathVariable("brandCode") String brandCode){
        try {
            brandService.deleteBrand(brandCode);
            WebResponse<String> response = WebResponse.<String>builder()
                    .status("success")
                    .data("Brand successfully deleted")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<String> errorResponse = WebResponse.<String>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
