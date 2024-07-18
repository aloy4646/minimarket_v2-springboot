package com.appschef.intern.minimarket.controller;

import com.appschef.intern.minimarket.dto.request.stock.UpdateStockRequest;
import com.appschef.intern.minimarket.dto.response.WebResponse;
import com.appschef.intern.minimarket.dto.response.stock.StockReportResponse;
import com.appschef.intern.minimarket.dto.response.stock.XXStockReportResponse;
import com.appschef.intern.minimarket.dto.response.stock.StockResponse;
import com.appschef.intern.minimarket.enumMessage.ErrorDateMessage;
import com.appschef.intern.minimarket.service.StockService;
import com.appschef.intern.minimarket.service.ValidationService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    private StockService stockService;

    @Autowired
    private ValidationService validationService;

    @PatchMapping("/{productCode}")
        public ResponseEntity<WebResponse<StockResponse>> updateStock(@PathVariable("productCode") String productCode,
                                                                      @Valid @RequestBody UpdateStockRequest updateStockRequest,
                                                                      BindingResult bindingResult){

        validationService.validate(bindingResult);

        try{
            StockResponse stockResponse = stockService.updateStock(productCode, updateStockRequest);
            WebResponse<StockResponse> response = WebResponse.<StockResponse>builder()
                    .status("success")
                    .data(stockResponse)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<StockResponse> errorResponse = WebResponse.<StockResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productCode}")
    public ResponseEntity<WebResponse<StockResponse>> getStockProduct(@PathVariable("productCode") String productCode){
        try{
            StockResponse stockResponse = stockService.getStockProduct(productCode);
            WebResponse<StockResponse> response =  WebResponse.<StockResponse>builder()
                    .status("success")
                    .data(stockResponse)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<StockResponse> errorResponse = WebResponse.<StockResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/report/{productCode}")
    public ResponseEntity<WebResponse<Page<StockReportResponse>>> topProduk(
            @PathVariable("productCode") String productCode,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "start_date", required = false) String startDateString,
            @RequestParam(name = "end_date", required = false) String endDateString
    ){
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "requestParams");

        if (startDateString == null || endDateString == null) {
            throw new ValidationException(ErrorDateMessage.START_OR_END_DATE_NULL.getMessage());
        }

        //validasi tanggal
        validateDate(startDateString, "startDate", bindingResult);
        validateDate(endDateString, "endDate", bindingResult);

        validationService.validate(bindingResult);

        try{
            Date startDate = parseDate(startDateString);
            Date endDate = parseDate(endDateString);

            Page<StockReportResponse> stockReportResponsePage = stockService.getStockReport(PageRequest.of(page, size), productCode, startDate, endDate);
            WebResponse<Page<StockReportResponse>> response = WebResponse.<Page<StockReportResponse>>builder()
                    .status("success")
                    .data(stockReportResponsePage)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<Page<StockReportResponse>> errorResponse = WebResponse.<Page<StockReportResponse>>builder()
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
