package com.group4.FKitShop.Entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Data // @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Supporting")
public class Supporting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supportingID")
    int supportingID;

    @Column(name = "accountID")
    String accountID;

    @Column(name = "orderDetailsID")
    String orderDetailsID;

    @Column(name = "countdown")
    int countdown;

    @Column(name = "supportingCount")
    int supportingCount;

    @Column(name = "status")
    int status;

    @Column(name = "postDate")
    Date postDate;

    @Column(name = "supportDate")
    Date supportDate;

}
