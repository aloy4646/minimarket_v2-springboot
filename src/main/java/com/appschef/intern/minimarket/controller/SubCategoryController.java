package com.appschef.intern.minimarket.controller;

import com.appschef.intern.minimarket.dto.request.subCategories.AddSubCategoryRequest;
import com.appschef.intern.minimarket.dto.request.subCategories.UpdateSubCategoryRequest;
import com.appschef.intern.minimarket.dto.response.WebResponse;
import com.appschef.intern.minimarket.dto.response.subCategory.SubCategoryDetailResponse;
import com.appschef.intern.minimarket.service.SubCategoryService;
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

@RestController
@RequestMapping("/sub-categories")
public class SubCategoryController {
    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private ValidationService validationService;

    @PostMapping("/")
    public ResponseEntity<WebResponse<SubCategoryDetailResponse>> createSubCategory(@Valid @RequestBody AddSubCategoryRequest addSubCategoryRequest, BindingResult bindingResult){
        validationService.validate(bindingResult);

        try{
            SubCategoryDetailResponse savedSubCategory = subCategoryService.addSubCategory(addSubCategoryRequest);
            WebResponse<SubCategoryDetailResponse> response = WebResponse.<SubCategoryDetailResponse>builder()
                    .status("success")
                    .data(savedSubCategory)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<SubCategoryDetailResponse> errorResponse = WebResponse.<SubCategoryDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{subCategoryCode}")
    public ResponseEntity<WebResponse<SubCategoryDetailResponse> >getSubCategory(@PathVariable("subCategoryCode") String subCategoryCode){
        try{
            SubCategoryDetailResponse promoResponseDTO = subCategoryService.getSubCategoryBySubCategoryCode(subCategoryCode);
            WebResponse<SubCategoryDetailResponse> response =  WebResponse.<SubCategoryDetailResponse>builder()
                    .status("success")
                    .data(promoResponseDTO)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<SubCategoryDetailResponse> errorResponse = WebResponse.<SubCategoryDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<WebResponse<Page<SubCategoryDetailResponse>>> getAllSubCategory(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try{
            Page<SubCategoryDetailResponse> listSubCategory = subCategoryService.getAllSubCategory(PageRequest.of(page, size));
            WebResponse<Page<SubCategoryDetailResponse>> response =  WebResponse.<Page<SubCategoryDetailResponse>>builder()
                    .status("success")
                    .data(listSubCategory)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e) {
            WebResponse<Page<SubCategoryDetailResponse>> errorResponse = WebResponse.<Page<SubCategoryDetailResponse>>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{subCategoryCode}")
    public ResponseEntity<WebResponse<SubCategoryDetailResponse>> updateSubCategory(@PathVariable("subCategoryCode") String subCategoryCode,
                                                                          @Valid @RequestBody UpdateSubCategoryRequest updateSubCategoryRequest,
                                                                          BindingResult bindingResult){

        validationService.validate(bindingResult);

        try{
            SubCategoryDetailResponse promoResponseDTO = subCategoryService.updateSubCategory(subCategoryCode, updateSubCategoryRequest);
            WebResponse<SubCategoryDetailResponse> response = WebResponse.<SubCategoryDetailResponse>builder()
                    .status("success")
                    .data(promoResponseDTO)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<SubCategoryDetailResponse> errorResponse = WebResponse.<SubCategoryDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{subCategoryCode}")
    public ResponseEntity<WebResponse<String>> deleteMember(@PathVariable("subCategoryCode") String subCategoryCode){
        try{
            subCategoryService.deleteSubCategory(subCategoryCode);
            WebResponse<String> response = WebResponse.<String>builder()
                    .status("success")
                    .data("Subcategory successfully deleted")
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
