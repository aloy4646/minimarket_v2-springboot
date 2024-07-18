package com.appschef.intern.minimarket.controller;

import com.appschef.intern.minimarket.dto.request.promo.AddPromoRequest;
import com.appschef.intern.minimarket.dto.response.WebResponse;
import com.appschef.intern.minimarket.dto.response.promo.PromoDetailResponse;
import com.appschef.intern.minimarket.service.PromoService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/promo")
public class PromoController {
    @Autowired
    private PromoService promoService;

    @Autowired
    private ValidationService validationService;

    @PostMapping("/")
    public ResponseEntity<WebResponse<PromoDetailResponse>> createPromo(@Valid @RequestBody AddPromoRequest addPromoRequest, BindingResult bindingResult){

        //validasi tanggal
        validateTanggal(addPromoRequest.getStartDate(), "startDate", bindingResult);
        validateTanggal(addPromoRequest.getEndDate(), "endDate", bindingResult);

        validationService.validate(bindingResult);

        try{
            PromoDetailResponse savedPromo = promoService.addPromo(addPromoRequest);
            WebResponse<PromoDetailResponse> response = WebResponse.<PromoDetailResponse>builder()
                    .status("success")
                    .data(savedPromo)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<PromoDetailResponse> errorResponse = WebResponse.<PromoDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{promoCode}")
    public ResponseEntity<WebResponse<PromoDetailResponse>> getPromo(@PathVariable("promoCode") String promoCode){
        try{
            PromoDetailResponse promoResponseDTO = promoService.getPromoByPromoCode(promoCode);
            WebResponse<PromoDetailResponse> response =  WebResponse.<PromoDetailResponse>builder()
                    .status("success")
                    .data(promoResponseDTO)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<PromoDetailResponse> errorResponse = WebResponse.<PromoDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<WebResponse<Page<PromoDetailResponse>>> getAllPromo(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try{
            Page<PromoDetailResponse> listPromo = promoService.getAllPromo(PageRequest.of(page, size));
            WebResponse<Page<PromoDetailResponse>> response =  WebResponse.<Page<PromoDetailResponse>>builder()
                    .status("success")
                    .data(listPromo)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e) {
            WebResponse<Page<PromoDetailResponse>> errorResponse = WebResponse.<Page<PromoDetailResponse>>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PutMapping("/{promoCode}")
//    public ResponseEntity<WebResponse<PromoDetailResponse>> updatePromo(@PathVariable("promoCode") String promoCode,
//                                                                          @Valid @RequestBody UpdatePromoRequest updatePromoRequest,
//                                                                          BindingResult bindingResult){
//
//        //validasi tanggal
//        validateTanggal(updatePromoRequest.getTanggalMulai(), "tanggalMulai", bindingResult);
//        validateTanggal(updatePromoRequest.getTanggalBerakhir(), "tanggalBerakhir", bindingResult);
//
//        validationService.validate(bindingResult);
//
//        try{
//            PromoDetailResponse promoResponseDTO = promoService.updatePromo(promoCode, updatePromoRequest);
//            WebResponse<PromoDetailResponse> response = WebResponse.<PromoDetailResponse>builder()
//                    .status("success")
//                    .data(promoResponseDTO)
//                    .build();
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }catch (ResponseStatusException e) {
//            throw e;
//        } catch (Exception e){
//            WebResponse<PromoDetailResponse> errorResponse = WebResponse.<PromoDetailResponse>builder()
//                    .status("fail")
//                    .error(e.getMessage())
//                    .build();
//            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @DeleteMapping("/{promoCode}")
//    public ResponseEntity<WebResponse<String>> deleteMember(@PathVariable("promoCode") String promoCode){
//        try{
//            promoService.deletePromo(promoCode);
//            WebResponse<String> response = WebResponse.<String>builder()
//                    .status("success")
//                    .data("Promo successfully deleted")
//                    .build();
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }catch (ResponseStatusException e) {
//            throw e;
//        } catch (Exception e){
//            WebResponse<String> errorResponse = WebResponse.<String>builder()
//                    .status("fail")
//                    .error(e.getMessage())
//                    .build();
//            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    private void validateTanggal(String tanggal, String fieldName, BindingResult bindingResult) {
        if (tanggal != null && !tanggal.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(tanggal);
            } catch (ParseException e) {
                bindingResult.rejectValue(fieldName, fieldName+".invalid", "Format " + fieldName + " tidak valid");
            }
        }
    }
}
