package com.appschef.intern.minimarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", nullable = false, unique = true)
    @Size(max = 13)
    private String productCode;

    @Column(name = "product_name", nullable = false)
    @Size(max = 100)
    private String productName;

    @Column(name = "price", columnDefinition = "DECIMAL", nullable = false)
    private BigDecimal price;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP  with time zone", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP  with time zone")
    private Date updatedAt;

    @OneToMany(mappedBy = "product")
    private List<OrdersDetail> listOrdersDetail;

    @OneToMany(mappedBy = "product")
    private List<Promo> listPromo;

    @OneToMany(mappedBy = "product")
    private List<LogStock> listLogStock;

    @ManyToOne
    @JoinColumn(name = "subcategories_id", referencedColumnName = "id")
    private SubCategories subCategories;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;
}
