package com.group4.FKitShop.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Blog")
public class Blog {

    @Id
    @Column(name = "blogID")
    String blogID;

    @Column(name = "blogName")
    String blogName;


    @Column(name = "content")
    String content;

    @Column(name = "status")
    String status;

    @Column(name = "image")
    String image;

    @Column(name = "accountID")
    String accountID;

    @Column(name = "createDate")
    Date createDate;

}
