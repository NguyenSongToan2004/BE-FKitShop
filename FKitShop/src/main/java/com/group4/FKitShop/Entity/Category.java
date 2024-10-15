package com.group4.FKitShop.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data   // @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "Category")
public class Category {

    @Id
    //  @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "categoryID")
    private String categoryID;

    @Column(name = "tagID")
    private int tagID;

    @Column(name = "categoryName")
    private String categoryName;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private int status;

}
