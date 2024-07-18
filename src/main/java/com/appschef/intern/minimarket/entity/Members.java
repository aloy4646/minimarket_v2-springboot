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
@Table(name = "members")
public class Members {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_number", nullable = false, unique = true)
    @Size(max = 7)
    private String memberNumber;

    @Column(name = "full_name", nullable = false)
    @Size(max = 100)
    private String fullName;

    @Column(name = "email", nullable = false)
    @Size(max = 360)
    private String email;

    @Column(name = "phone_number", nullable = false)
    @Size(max = 13)
    private String phoneNumber;

    @Column(name = "address", columnDefinition = "TEXT", nullable = false)
    private String address;

    @Column(name = "point")
    private Long point;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP  with time zone", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP  with time zone")
    private Date updatedAt;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(mappedBy = "members")
    private List<Orders> listOrders;
}
