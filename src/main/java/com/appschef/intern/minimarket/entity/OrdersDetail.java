package com.appschef.intern.minimarket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders_detail")
public class OrdersDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_price", columnDefinition = "DECIMAL" , nullable = false)
    private BigDecimal productPrice;

    @Column(name = "product_final_price", columnDefinition = "DECIMAL", nullable = false)
    private BigDecimal productFinalPrice;

    @ManyToOne
    @JoinColumn(name = "orders_id", referencedColumnName = "id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToMany(mappedBy = "ordersDetail")
    private List<OrdersPromo> listOrdersPromo;
}
