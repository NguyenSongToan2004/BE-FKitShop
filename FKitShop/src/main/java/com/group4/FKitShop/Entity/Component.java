package com.group4.FKitShop.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Component")
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "componentIndex")
    int componentIndex;

    @Column(name = "componentID")
    String componentID;

    @Column(name = "productID")
    String productID;

    @Column(name = "quantity")
    int quantity;
}