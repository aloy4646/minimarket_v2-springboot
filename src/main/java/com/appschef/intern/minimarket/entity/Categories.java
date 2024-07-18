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
@Table(name = "categories")
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_code", nullable = false, unique = true)
    @Size(max = 7)
    private String categoryCode;

    @Column(name = "category_name", nullable = false)
    @Size(max = 100)
    private String categoryName;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP  with time zone", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP  with time zone")
    private Date updatedAt;

    @OneToMany(mappedBy = "categories")
    private List<SubCategories> listSubCategories;
}
