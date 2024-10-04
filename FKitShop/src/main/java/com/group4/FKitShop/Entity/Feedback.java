package com.group4.FKitShop.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data // @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedbackID")
    int feedbackID;

    @Column(name = "accountID")
    String accountID;

    @Column(name = "productID")
    String productID;

    @Column(name = "description")
    String description;

    @Column(name = "rate")
    int rate;
    // 1-> 5 star

}
