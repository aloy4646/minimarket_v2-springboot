package com.appschef.intern.minimarket.service;

import com.appschef.intern.minimarket.dto.request.stock.UpdateStockRequest;
import com.appschef.intern.minimarket.dto.response.stock.StockReportResponse;
import com.appschef.intern.minimarket.dto.response.stock.XXStockReportResponse;
import com.appschef.intern.minimarket.dto.response.stock.StockResponse;
import com.appschef.intern.minimarket.entity.LogStock;
import com.appschef.intern.minimarket.entity.Product;
import com.appschef.intern.minimarket.enumMessage.*;
import com.appschef.intern.minimarket.projection.StockReportProjection;
import com.appschef.intern.minimarket.repository.ProductRepository;
import com.appschef.intern.minimarket.repository.StockRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class StockService {
    @Autowired
    StockRepository stockRepository;

    @Autowired
    ProductRepository productRepository;

    @Transactional
    public StockResponse updateStock(String productCode, UpdateStockRequest updateStockRequest){
        Product product = productRepository.findByProductCodeAndIsDeletedFalse(productCode)
                .orElseThrow(() -> new ValidationException(PromoErrorMessage.NOT_FOUND.getMessage()));

        if(!updateStockRequest.getType().equals(UpdateStockType.ADDITION.toString()) &&
                !updateStockRequest.getType().equals(UpdateStockType.EXPIRATION.toString())){
            throw new ValidationException(StockErrorMessage.INVALID_TYPE.getMessage());
        }

        if((updateStockRequest.getType().equals(UpdateStockType.ADDITION.toString()) && updateStockRequest.getQuantity() < 0)
            || (updateStockRequest.getType().equals(UpdateStockType.EXPIRATION.toString()) && updateStockRequest.getQuantity() > 0)){
            throw new ValidationException(StockErrorMessage.TYPE_QUANTITY_MISMATCH.getMessage());
        }

        int currentStock = product.getCurrentStock() + updateStockRequest.getQuantity();
        if (currentStock < 0){
            throw new ValidationException(StockErrorMessage.EXCEEDS_CURRENT_STOCK.getMessage());
        }

        product.setCurrentStock(currentStock);
        productRepository.saveAndFlush(product);

        stockRepository.save(new LogStock(
                null,
                new Date(),
                updateStockRequest.getType(),
                updateStockRequest.getQuantity(),
                product
        ));

        return new StockResponse(
                product.getProductCode(),
                product.getProductName(),
                product.getCurrentStock()
        );
    }

    public StockResponse getStockProduct(String productCode){
        Product product = productRepository.findByProductCodeAndIsDeletedFalse(productCode)
                .orElseThrow(() -> new ValidationException(PromoErrorMessage.NOT_FOUND.getMessage()));

        return new StockResponse(
                product.getProductCode(),
                product.getProductName(),
                product.getCurrentStock()
        );
    }

    public Page<StockReportResponse> getStockReport(Pageable pageable, String productCode, Date startDate, Date endDate){
        Product product = productRepository.findByProductCodeAndIsDeletedFalse(productCode)
                .orElseThrow(() -> new ValidationException(ProductErrorMessage.NOT_FOUND.getMessage()));

        if (startDate.after(endDate)) {
            throw new ValidationException(ErrorDateMessage.INVALID_END_DATE.getMessage());
        }

        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        List<StockReportResponse> stockReportResponsesResponseList = new ArrayList<>();
        StockReportProjection stockReportProjection;

        //iterasi startDate sampai endDate
        for (LocalDate date = localStartDate; !date.isAfter(localEndDate); date = date.plusDays(1)) {
            stockReportProjection = stockRepository.getStockReport(date, product.getId());

            if(stockReportProjection == null){
                stockReportResponsesResponseList.add(
                        new StockReportResponse(
                                date,
                                0L,
                                0L,
                                0L
                        )
                );
            }else{
                stockReportResponsesResponseList.add(
                        new StockReportResponse(
                                date,
                                stockReportProjection.getTotalSales(),
                                stockReportProjection.getTotalExpired(),
                                stockReportProjection.getTotalAdditions()
                        )
                );
            }
        }

        int start = Math.min((int) pageable.getOffset(), stockReportResponsesResponseList.size());
        int end = Math.min((start + pageable.getPageSize()), stockReportResponsesResponseList.size());
        return new PageImpl<>(stockReportResponsesResponseList.subList(start, end), pageable, stockReportResponsesResponseList.size());
    }
}
