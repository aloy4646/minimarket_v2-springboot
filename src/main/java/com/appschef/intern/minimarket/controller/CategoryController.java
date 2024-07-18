package com.appschef.intern.minimarket.controller;

import com.appschef.intern.minimarket.dto.request.categories.AddCategoryRequest;
import com.appschef.intern.minimarket.dto.request.categories.UpdateCategoryRequest;
import com.appschef.intern.minimarket.dto.response.WebResponse;
import com.appschef.intern.minimarket.dto.response.categories.CategoryDetailResponse;
import com.appschef.intern.minimarket.service.CategoriesService;
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
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoriesService categoryService;

    @Autowired
    private ValidationService validationService;

    @PostMapping("/")
    public ResponseEntity<WebResponse<CategoryDetailResponse>> createCategory(@Valid @RequestBody AddCategoryRequest addCategoryRequest, BindingResult bindingResult){
        validationService.validate(bindingResult);

        try{
            CategoryDetailResponse savedCategory = categoryService.addCategory(addCategoryRequest);
            WebResponse<CategoryDetailResponse> response = WebResponse.<CategoryDetailResponse>builder()
                    .status("success")
                    .data(savedCategory)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<CategoryDetailResponse> errorResponse = WebResponse.<CategoryDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{categoryCode}")
    public ResponseEntity<WebResponse<CategoryDetailResponse> >getCategory(@PathVariable("categoryCode") String categoryCode){
        try{
            CategoryDetailResponse promoResponseDTO = categoryService.getCategoryByCategoryCode(categoryCode);
            WebResponse<CategoryDetailResponse> response =  WebResponse.<CategoryDetailResponse>builder()
                    .status("success")
                    .data(promoResponseDTO)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<CategoryDetailResponse> errorResponse = WebResponse.<CategoryDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<WebResponse<Page<CategoryDetailResponse>>> getAllCategory(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try{
            Page<CategoryDetailResponse> listCategory = categoryService.getAllCategory(PageRequest.of(page, size));
            WebResponse<Page<CategoryDetailResponse>> response =  WebResponse.<Page<CategoryDetailResponse>>builder()
                    .status("success")
                    .data(listCategory)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e) {
            WebResponse<Page<CategoryDetailResponse>> errorResponse = WebResponse.<Page<CategoryDetailResponse>>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{categoryCode}")
    public ResponseEntity<WebResponse<CategoryDetailResponse>> updateCategory(@PathVariable("categoryCode") String categoryCode,
                                                                          @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest,
                                                                          BindingResult bindingResult){

        validationService.validate(bindingResult);

        try{
            CategoryDetailResponse promoResponseDTO = categoryService.updateCategory(categoryCode, updateCategoryRequest);
            WebResponse<CategoryDetailResponse> response = WebResponse.<CategoryDetailResponse>builder()
                    .status("success")
                    .data(promoResponseDTO)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<CategoryDetailResponse> errorResponse = WebResponse.<CategoryDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{categoryCode}")
    public ResponseEntity<WebResponse<String>> deleteMember(@PathVariable("categoryCode") String categoryCode){
        try{
            categoryService.deleteCategory(categoryCode);
            WebResponse<String> response = WebResponse.<String>builder()
                    .status("success")
                    .data("Category successfully deleted")
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