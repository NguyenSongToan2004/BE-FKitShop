package com.group4.FKitShop.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Own {

    @EmbeddedId
    OwnID ownID;

    // Getters and Setters
    @ManyToOne
    @JsonBackReference
    @MapsId("labID")
    @JoinColumn(name = "labID", updatable = false)
    Lab lab;

    @ManyToOne
    @JsonBackReference
    @MapsId("accountID")
    @JoinColumn(name = "accountID", updatable = false)
    Accounts account;

    @NotNull
    int supportTimes;

    // Constructors, getters, and setters
}
