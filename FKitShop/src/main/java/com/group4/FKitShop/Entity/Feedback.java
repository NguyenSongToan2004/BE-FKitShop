package com.group4.FKitShop.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Entity
@Data // @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
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

    @Column(name = "createDate")
    Date createDate;


}
