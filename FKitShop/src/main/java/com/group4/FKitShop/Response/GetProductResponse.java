package com.group4.FKitShop.Response;


import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Entity.Image;
import com.group4.FKitShop.Entity.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetProductResponse {
    String productID;
    String name;
    String description;
    String publisher;
    int quantity;
    double price;
    int discount;
    String status;
    double weight;
    String material;
    int unitOnOrder;
    String type;
    String dimension;
    Date createDate;
    List<Image> images;
    List<Category> categories;
    List<ComponentResponse> components;
}
