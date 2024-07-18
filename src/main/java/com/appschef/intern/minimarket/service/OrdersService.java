package com.appschef.intern.minimarket.service;

import com.appschef.intern.minimarket.dto.request.orders.OrderRequest;
import com.appschef.intern.minimarket.dto.request.product.ProductOrderRequest;
import com.appschef.intern.minimarket.dto.response.members.TopMemberResponse;
import com.appschef.intern.minimarket.dto.response.orders.DailySalesResponse;
import com.appschef.intern.minimarket.dto.response.orders.OrderResponse;
import com.appschef.intern.minimarket.dto.response.orders.TopOrdersMemberResponse;
import com.appschef.intern.minimarket.dto.response.product.ProductOrderResponse;
import com.appschef.intern.minimarket.dto.response.product.TopProductResponse;
import com.appschef.intern.minimarket.dto.response.promo.PromoReportResponse;
import com.appschef.intern.minimarket.entity.*;
import com.appschef.intern.minimarket.enumMessage.*;
import com.appschef.intern.minimarket.mapper.MemberMapper;
import com.appschef.intern.minimarket.mapper.OrdersPromoMapper;
import com.appschef.intern.minimarket.projection.DailySalesProjection;
import com.appschef.intern.minimarket.projection.TopMemberProjection;
import com.appschef.intern.minimarket.projection.TopProductProjection;
import com.appschef.intern.minimarket.repository.*;
import com.appschef.intern.minimarket.util.ApplicationProperties;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private OrdersDetailRepository ordersDetailRepository;

    @Autowired
    private OrdersPromoRepository ordersPromoRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Transactional
    public OrderResponse addOrder(OrderRequest orderRequest) {
        Members members = null;
        if(orderRequest.getNomorMember() != null && !orderRequest.getNomorMember().isEmpty()){
            members = membersRepository.findByMemberNumberAndIsDeletedFalse(orderRequest.getNomorMember())
                    .orElseThrow(() -> new ValidationException(MembersErrorMessage.NOT_FOUND.getMessage()));
        }

        Orders orders = new Orders(null, null, new Date(), members, null);

        // Mendapatkan tanggal saat ini
        LocalDate currentDate = LocalDate.now();
        String format = currentDate.format(DateTimeFormatter.ofPattern("ddMMyy"));

        String lastReceiptNumber = ordersRepository.getLastReceiptNumber((format + "%"));
        if(lastReceiptNumber == null){
            orders.setReceiptNumber(format + "0001");
        }else{
            String lastThreeNumber = lastReceiptNumber.substring(lastReceiptNumber.length() - 4);
            Long newReceiptNumberLong = Long.parseLong(lastThreeNumber);
            newReceiptNumberLong++;
            String newReceiptNumber = format + String.format("%04d", newReceiptNumberLong);
            orders.setReceiptNumber(newReceiptNumber);
        }

        ordersRepository.saveAndFlush(orders);

        List<ProductOrderResponse> listProductOrders = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        //iterasi listProdukOrders
        for(ProductOrderRequest productOrdersRequest : orderRequest.getOrderProductList()){
            Product product = productRepository.findByProductCodeAndIsDeletedFalse(productOrdersRequest.getProductCode())
                    .orElseThrow(() -> new ValidationException(ProductErrorMessage.NOT_FOUND.getMessage()));

            //cek stock produk
            int currentStock = product.getCurrentStock() - productOrdersRequest.getQuantity();
            if(currentStock < 0){
                throw new ValidationException(product.getProductCode() + " - " + product.getProductName() + ": " + StockErrorMessage.EXCEEDS_CURRENT_STOCK.getMessage());
            }
            product.setCurrentStock(currentStock);
            productRepository.save(product);

            stockRepository.save(new LogStock(
                    null,
                    new Date(),
                    UpdateStockType.SALE.toString(),
                    -(productOrdersRequest.getQuantity()),
                    product
            ));

            //cek promo produk
            List<Promo> listPromo = promoRepository.findValidPromoByProduct(orders.getOrderDate(), product);

            BigDecimal productFinalPrice = product.getPrice();
            List<OrdersPromo> listOrdersPromo = new ArrayList<>();
            //menghitung harga produk setelah promo
            for(Promo promo : listPromo){
                if(Objects.equals(promo.getPromoType(), PromoType.PERCENT.getType())){
                    productFinalPrice = productFinalPrice.subtract(productFinalPrice.multiply(promo.getPromoValue()));
                }else if (Objects.equals(promo.getPromoType(), PromoType.FLAT_AMOUNT.getType())){
                    productFinalPrice = productFinalPrice.subtract(promo.getPromoValue());
                }

                listOrdersPromo.add(OrdersPromoMapper.mapToOrdersPromo(promo));
            }

            productFinalPrice = productFinalPrice.setScale(2, RoundingMode.HALF_UP);

            if (productFinalPrice.compareTo(BigDecimal.ZERO) < 0) {
                productFinalPrice = BigDecimal.ZERO;
            }

            OrdersDetail ordersDetail = new OrdersDetail(
                    null,
                    productOrdersRequest.getQuantity(),
                    product.getProductCode(),
                    product.getProductName(),
                    product.getPrice(),
                    productFinalPrice,
                    orders,
                    product,
                    listOrdersPromo
            );

            ordersDetailRepository.saveAndFlush(ordersDetail);

            for (OrdersPromo ordersPromo : listOrdersPromo){
                ordersPromo.setOrdersDetail(ordersDetail);
                ordersPromoRepository.save(ordersPromo);
            }

            BigDecimal jumlahProdukBigDecimal = BigDecimal.valueOf(productOrdersRequest.getQuantity());
            BigDecimal totalHargaProduk = productFinalPrice.multiply(jumlahProdukBigDecimal);

            //potongan harga
            BigDecimal discount = product.getPrice().subtract(productFinalPrice);

            //penambahan harga keseluruhan
            totalPrice = totalPrice.add(totalHargaProduk);

            listProductOrders.add(new ProductOrderResponse(
                    product.getProductCode(),
                    product.getProductName(),
                    productFinalPrice,
                    discount,
                    listOrdersPromo.stream().map(OrdersPromoMapper::mapToPromoOrderResponse).toList(),
                    productOrdersRequest.getQuantity(),
                    totalHargaProduk
            ));
        }

        OrderResponse orderResponse = new OrderResponse(
                null,
                orders.getReceiptNumber(),
                listProductOrders,
                totalPrice
        );

        if (members != null){
            //tambah poin member
            members.setPoint(members.getPoint() + applicationProperties.getTransactionPoint());
            membersRepository.save(members);

            orderResponse.setMemberNumber(members.getMemberNumber());
        }

        return orderResponse;
    }

    public DailySalesResponse getDailySalesProjection(Date date){
        DailySalesProjection dailySalesProjection = ordersRepository.getDailySalesReport(date);

        DailySalesResponse dailySalesResponse;
        if(dailySalesProjection == null){
            dailySalesResponse = new DailySalesResponse(
                    date,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
        }else {
            BigDecimal selisih = dailySalesProjection.getTotalGrossSales().subtract(dailySalesProjection.getTotalNetSales());
            dailySalesResponse = new DailySalesResponse(
                    date,
                    dailySalesProjection.getTotalGrossSales(),
                    selisih,
                    dailySalesProjection.getTotalNetSales()
            );
        }

        return dailySalesResponse;
    }

    public List<TopMemberResponse> getTopMemberByTotalNominalOrders(){
        List<TopMemberProjection> listTopMemberProjection = membersRepository.getTopMember();

        return listTopMemberProjection.stream().map(MemberMapper::mapToTopMemberResponse).toList();
    }

    public TopOrdersMemberResponse getTopOrdersMember(String memberNumber){
        Members members = membersRepository.findByMemberNumberAndIsDeletedFalse(memberNumber)
                .orElseThrow(() -> new ValidationException(MembersErrorMessage.NOT_FOUND.getMessage()));

        List<TopProductProjection> listTopProductProjection = productRepository.getTopProductMember(memberNumber);

        List<TopProductResponse> listTopProductResponse = setProductNameToTopProdukResponse(listTopProductProjection);

        return new TopOrdersMemberResponse(
                members.getMemberNumber(),
                members.getFullName(),
                listTopProductResponse
        );
    }

    public List<TopProductResponse> getTopProduct(Date startDate, Date endDate){
        if (startDate.after(endDate)) {
            throw new ValidationException(ErrorDateMessage.INVALID_END_DATE.getMessage());
        }

        List<TopProductProjection> listTopProductProjection = productRepository.getTopProduct(startDate, endDate);

        return setProductNameToTopProdukResponse(listTopProductProjection);
    }

    private List<TopProductResponse> setProductNameToTopProdukResponse(List<TopProductProjection> listTopProductResponse){
        List<TopProductResponse> listProductResponse = new ArrayList<>();
        for (TopProductProjection topProductProjection : listTopProductResponse){
            String namaProduk = productRepository.findProductNameByProductCode(topProductProjection.getProductCode());
            listProductResponse.add(new TopProductResponse(
                    topProductProjection.getProductCode(),
                    namaProduk,
                    topProductProjection.getNumberOfPurchases())
            );
        }

        return listProductResponse;
    }

    public PromoReportResponse getPromoReport (String promoCode){
        Promo promo = promoRepository.findByPromoCode(promoCode)
                .orElseThrow(() -> new ValidationException(PromoErrorMessage.NOT_FOUND.getMessage()));

        Long jumlahPemakaian = promoRepository.getNumberOfPromoUsages(promoCode);

        List<TopMemberProjection> listMemberPemakai = promoRepository.topPromoUsingMembers(promoCode);

        return new PromoReportResponse(
                promo.getPromoCode(),
                promo.getPromoName(),
                promo.getProduct().getProductCode(),
                promo.getProduct().getProductName(),
                jumlahPemakaian,
                listMemberPemakai.stream().map(MemberMapper::mapToTopMemberResponse).toList()
        );
    }

}