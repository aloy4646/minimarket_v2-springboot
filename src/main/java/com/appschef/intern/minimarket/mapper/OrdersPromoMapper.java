package com.appschef.intern.minimarket.mapper;

import com.appschef.intern.minimarket.dto.response.promo.PromoOrderResponse;
import com.appschef.intern.minimarket.entity.OrdersPromo;
import com.appschef.intern.minimarket.entity.Promo;

public class OrdersPromoMapper {

    public static OrdersPromo mapToOrdersPromo(Promo promo){
        return new OrdersPromo(
                null,
                promo.getId(),
                promo.getPromoCode(),
                promo.getPromoName(),
                promo.getPromoType(),
                promo.getPromoValue(),
                null
        );
    }

    public static PromoOrderResponse mapToPromoOrderResponse(OrdersPromo ordersPromo){

        return new PromoOrderResponse(
                ordersPromo.getPromoCode(),
                ordersPromo.getPromoName(),
                ordersPromo.getPromoType(),
                ordersPromo.getPromoValue()
        );
    }
}
