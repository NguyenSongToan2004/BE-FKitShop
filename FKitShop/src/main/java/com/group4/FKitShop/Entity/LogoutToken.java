package com.group4.FKitShop.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "LogoutToken")
public class LogoutToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logoutTokenID")
    int logoutTokenID;
    @Column(name = "accountID")
    String accountID;
    @Column(name = "jwtID")
    String jwtID;
    @Column(name = "expiredDate")
    @Temporal(TemporalType.DATE)
    Date expiredDate;
}
