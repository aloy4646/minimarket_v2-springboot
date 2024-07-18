package com.appschef.intern.minimarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "promo")
public class Promo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "promo_code", nullable = false, unique = true)
    @Size(max = 7)
    private String promoCode;

    @Column(name = "promo_name", nullable = false)
    @Size(max = 100)
    private String promoName;

    @Column(name = "promo_type", nullable = false)
    private String promoType;

    @Column(name = "promo_value", nullable = false)
    private BigDecimal promoValue;

    @Column(name = "start_date", columnDefinition = "TIMESTAMP with time zone", nullable = false)
    private Date startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP with time zone", nullable = false)
    private Date endDate;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP  with time zone", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
}
