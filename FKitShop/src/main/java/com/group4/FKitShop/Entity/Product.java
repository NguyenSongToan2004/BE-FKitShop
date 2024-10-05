package com.group4.FKitShop.Entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "StemProduct")
public class Product {
    @Id
    @NotNull
    @Length(min = 1, max = 30)
    String productID;
    @NotNull
    @Length(min = 1, max = 100, message = "Product name has length from 1 to 100 characters")
    String name;

    String description;
    @Length(max = 100, message = "Content Description has less than 101 characters")
    String publisher;
    @NotNull
    int quantity;
    double price;

    int discount;
    @NotNull
    @Length(min = 0, max = 10, message = "status is not more than 10 characters")
    String status;
    double weight;
    @Length(max = 100, message = "Material length is less than 101 characters")
    String material;
    @NotNull
    int unitOnOrder;

    @NotNull
    @Length(min = 0, max = 10, message = "Type length is not more than 10 characters")
    String type;

    @Length(max = 100, message = "Dimension length is not more than 100 characters")
    String dimension;
    @NotNull
    Date createDate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Image> images;
}
