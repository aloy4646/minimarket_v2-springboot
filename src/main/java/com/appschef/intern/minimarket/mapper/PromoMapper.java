package com.appschef.intern.minimarket.mapper;

import com.appschef.intern.minimarket.dto.request.promo.AddPromoRequest;
import com.appschef.intern.minimarket.dto.response.promo.PromoDetailResponse;
import com.appschef.intern.minimarket.entity.Promo;

import java.util.Date;

public class PromoMapper {
    public static Promo mapToPromo(AddPromoRequest addPromoRequest){

        return new Promo(
                null,
                addPromoRequest.getPromoCode(),
                addPromoRequest.getPromoName(),
                addPromoRequest.getType(),
                addPromoRequest.getValue(),
                null,
                null,
                new Date(),
                null
        );
    }

    public static PromoDetailResponse mapToPromoDetailResponse(Promo promo){

        return new PromoDetailResponse(
                promo.getPromoCode(),
                promo.getPromoName(),
                promo.getProduct().getProductCode(),
                promo.getProduct().getProductName(),
                promo.getPromoType(),
                promo.getPromoValue(),
                promo.getStartDate(),
                promo.getEndDate()
        );
    }
}
