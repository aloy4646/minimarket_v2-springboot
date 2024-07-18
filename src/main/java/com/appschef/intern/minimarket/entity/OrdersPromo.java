package com.appschef.intern.minimarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders_promo")
public class OrdersPromo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "promo_id", nullable = false)
    private Long promoId;

    @Column(name = "promo_code", nullable = false)
    @Size(max = 7)
    private String promoCode;

    @Column(name = "promo_name", nullable = false)
    @Size(max = 100)
    private String promoName;

    @Column(name = "promo_type", nullable = false)
    private String promoType;

    @Column(name = "promo_value", nullable = false)
    private BigDecimal promoValue;

    @ManyToOne
    @JoinColumn(name = "orders_detail_id", referencedColumnName = "id")
    private OrdersDetail ordersDetail;
}
