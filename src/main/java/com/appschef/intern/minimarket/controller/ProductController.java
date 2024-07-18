package com.appschef.intern.minimarket.controller;

import com.appschef.intern.minimarket.dto.request.product.AddProductRequest;
import com.appschef.intern.minimarket.dto.request.product.UpdateProductRequest;
import com.appschef.intern.minimarket.dto.response.WebResponse;
import com.appschef.intern.minimarket.dto.response.product.ProductDetailResponse;
import com.appschef.intern.minimarket.service.ProductService;
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
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ValidationService validationService;

    @PostMapping("/")
    public ResponseEntity<WebResponse<ProductDetailResponse>> createProduct(@Valid @RequestBody AddProductRequest addProductRequest, BindingResult bindingResult){
        validationService.validate(bindingResult);

        try{
            ProductDetailResponse savedProduct = productService.addProduct(addProductRequest);
            WebResponse<ProductDetailResponse> response = WebResponse.<ProductDetailResponse>builder()
                    .status("success")
                    .data(savedProduct)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<ProductDetailResponse> errorResponse = WebResponse.<ProductDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productCode}")
    public ResponseEntity<WebResponse<ProductDetailResponse> >getProduct(@PathVariable("productCode") String productCode){
        try{
            ProductDetailResponse promoResponseDTO = productService.getProductByProductCode(productCode);
            WebResponse<ProductDetailResponse> response =  WebResponse.<ProductDetailResponse>builder()
                    .status("success")
                    .data(promoResponseDTO)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<ProductDetailResponse> errorResponse = WebResponse.<ProductDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<WebResponse<Page<ProductDetailResponse>>> getAllProduct(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "brand_code", required = false) String brandCode,
            @RequestParam(value = "subcategory_code", required = false) String subCategoryCode,
            @RequestParam(value = "category_code", required = false) String categoryCode,
            @RequestParam(value = "out_of_stock", required = false) Boolean outOfStock) {
        try{
            Page<ProductDetailResponse> listProduct = productService.getAllProduct(
                    PageRequest.of(page, size),
                    brandCode,
                    subCategoryCode,
                    categoryCode,
                    outOfStock);

            WebResponse<Page<ProductDetailResponse>> response =  WebResponse.<Page<ProductDetailResponse>>builder()
                    .status("success")
                    .data(listProduct)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e) {
            WebResponse<Page<ProductDetailResponse>> errorResponse = WebResponse.<Page<ProductDetailResponse>>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{productCode}")
    public ResponseEntity<WebResponse<ProductDetailResponse>> updateProduct(@PathVariable("productCode") String productCode,
                                                                   @Valid @RequestBody UpdateProductRequest updateProductRequest,
                                                                   BindingResult bindingResult){

        validationService.validate(bindingResult);

        try{
            ProductDetailResponse promoResponseDTO = productService.updateProduct(productCode, updateProductRequest);
            WebResponse<ProductDetailResponse> response = WebResponse.<ProductDetailResponse>builder()
                    .status("success")
                    .data(promoResponseDTO)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<ProductDetailResponse> errorResponse = WebResponse.<ProductDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{productCode}")
    public ResponseEntity<WebResponse<String>> deleteMember(@PathVariable("productCode") String productCode){
        try{
            productService.deleteProduct(productCode);
            WebResponse<String> response = WebResponse.<String>builder()
                    .status("success")
                    .data("Product successfully deleted")
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
