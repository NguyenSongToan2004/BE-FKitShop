package com.group4.FKitShop.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Supporting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String supportingID;

    @ManyToOne
    @JoinColumn(name = "accountID")
    @JsonBackReference
    Accounts account;

    @ManyToOne
    @JoinColumn(name = "orderDetailsID")
    @JsonBackReference
    OrderDetails orderDetail;

    @NotNull
    String description;

    @NotNull
    int status; // 0 : received, 1: approved, 2: done

    @NotNull
    Date postDate;

    Date supportDate;
}
