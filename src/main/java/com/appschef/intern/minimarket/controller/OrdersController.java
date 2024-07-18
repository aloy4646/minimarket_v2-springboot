package com.appschef.intern.minimarket.controller;

import com.appschef.intern.minimarket.dto.request.orders.OrderRequest;
import com.appschef.intern.minimarket.dto.response.WebResponse;
import com.appschef.intern.minimarket.dto.response.members.TopMemberResponse;
import com.appschef.intern.minimarket.dto.response.orders.DailySalesResponse;
import com.appschef.intern.minimarket.dto.response.orders.OrderResponse;
import com.appschef.intern.minimarket.dto.response.orders.TopOrdersMemberResponse;
import com.appschef.intern.minimarket.dto.response.product.TopProductResponse;
import com.appschef.intern.minimarket.dto.response.promo.PromoReportResponse;
import com.appschef.intern.minimarket.service.OrdersService;
import com.appschef.intern.minimarket.service.ValidationService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    OrdersService ordersService;

    @Autowired
    private ValidationService validationService;

    @PostMapping("/")
    public ResponseEntity<WebResponse<OrderResponse>> addPembelian(@Valid @RequestBody OrderRequest orderRequest,
                                                                   BindingResult bindingResult){

        validationService.validate(bindingResult);

        try{
            OrderResponse newOrder = ordersService.addOrder(orderRequest);
            WebResponse<OrderResponse> response = WebResponse.<OrderResponse>builder()
                    .status("success")
                    .data(newOrder)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<OrderResponse> errorResponse = WebResponse.<OrderResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/daily-sales")
    public ResponseEntity<WebResponse<DailySalesResponse>> dailySales(
            @RequestParam(name = "date", required = false) String date
    ){
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "requestParams");
        if (date == null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.format(new Date());
        }

        Date formattedDate = null;
        if (date != null && !date.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                formattedDate = dateFormat.parse(date);
            } catch (ParseException e) {
                bindingResult.rejectValue("date", "date.invalid", "Format date tidak valid");
            }
        }

        validationService.validate(bindingResult);

        try{
            DailySalesResponse penjualanPerHariResponse = ordersService.getDailySalesProjection(formattedDate);
            WebResponse<DailySalesResponse> response = WebResponse.<DailySalesResponse>builder()
                    .status("success")
                    .data(penjualanPerHariResponse)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<DailySalesResponse> errorResponse = WebResponse.<DailySalesResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/top-member")
    public ResponseEntity<WebResponse<List<TopMemberResponse>>> topMember () {
        try{
            List<TopMemberResponse> listTopMemberResponse = ordersService.getTopMemberByTotalNominalOrders();
            WebResponse<List<TopMemberResponse>> response = WebResponse.<List<TopMemberResponse>>builder()
                    .status("success")
                    .data(listTopMemberResponse)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<List<TopMemberResponse>> errorResponse = WebResponse.<List<TopMemberResponse>>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/top-orders/member/{memberNumber}")
    public ResponseEntity<WebResponse<TopOrdersMemberResponse>> topOrdersMember (@PathVariable("memberNumber") String memberNumber) {
        try{
            TopOrdersMemberResponse topPembelianMemberResponse = ordersService.getTopOrdersMember(memberNumber);
            WebResponse<TopOrdersMemberResponse> response = WebResponse.<TopOrdersMemberResponse>builder()
                    .status("success")
                    .data(topPembelianMemberResponse)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<TopOrdersMemberResponse> errorResponse = WebResponse.<TopOrdersMemberResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/top-product")
    public ResponseEntity<WebResponse<List<TopProductResponse>>> topProduk(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "start_date", defaultValue = "2024-07-01") String startDateString,
            @RequestParam(name = "end_date", required = false) String endDateString
    ){
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "requestParams");

        if (endDateString == null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            endDateString = dateFormat.format(new Date());
        }

        //validasi tanggal
        validateDate(startDateString, "startDate", bindingResult);
        validateDate(endDateString, "endDate", bindingResult);

        validationService.validate(bindingResult);

        try{
            Date startDate = parseDate(startDateString);
            Date endDate = parseDate(endDateString);

            List<TopProductResponse> topProdukResponse = ordersService.getTopProduct(startDate, endDate);
            WebResponse<List<TopProductResponse>> response = WebResponse.<List<TopProductResponse>>builder()
                    .status("success")
                    .data(topProdukResponse)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<List<TopProductResponse>> errorResponse = WebResponse.<List<TopProductResponse>>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/promo-report/{promoCode}")
    public ResponseEntity<WebResponse<PromoReportResponse>> getPromoReport(@PathVariable("promoCode") String promoCode){
        try{
            PromoReportResponse laporanPromoResponse = ordersService.getPromoReport(promoCode);
            WebResponse<PromoReportResponse> response = WebResponse.<PromoReportResponse>builder()
                    .status("success")
                    .data(laporanPromoResponse)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<PromoReportResponse> errorResponse = WebResponse.<PromoReportResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateDate(String date, String fieldName, BindingResult bindingResult) {
        if (date != null && !date.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(date);
            } catch (ParseException e) {
                bindingResult.rejectValue(fieldName, fieldName+".invalid", "Format " + fieldName + " tidak valid");
            }
        }
    }

    private Date parseDate(String date) throws ParseException {
        if (date == null || date.isEmpty()) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        return dateFormat.parse(date);
    }
}
