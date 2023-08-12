package com.sunset_cafe.sunset_cafe_backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "Product.getAllproducts",query = "select new com.sunset_cafe.sunset_cafe_backend.DTO.ProductDTO(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p")
@NamedQuery(name = "Product.getProductsByCategory",query = "select new com.sunset_cafe.sunset_cafe_backend.DTO.ProductDTO(p.id,p.name,p.description,p.price) from Product p where p.category.id=:id and p.status='true'")


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicUpdate
@DynamicInsert
@Table(name = "product")
public class Product implements Serializable{

    private static final long serialVersionUID = 123456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk",nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "status")
    private String status;

}
