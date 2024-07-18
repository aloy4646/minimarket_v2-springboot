package com.appschef.intern.minimarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receipt_number", nullable = false, unique = true)
    @Size(max = 10)
    private String receiptNumber;

    @Column(name = "order_date", columnDefinition = "TIMESTAMP with time zone", nullable = false)
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "members_id", referencedColumnName = "id")
    private Members members;

    @OneToMany(mappedBy = "orders")
    private List<OrdersDetail> listOrdersDetail;

}
