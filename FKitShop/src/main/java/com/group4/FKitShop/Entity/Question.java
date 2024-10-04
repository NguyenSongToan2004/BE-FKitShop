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
@Table(name = "Question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questionID")
    int questionID;

    @Column(name = "labID")
    String labID;

    @Column(name = "accountID")
    String accountID;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    int status;
    // 1 for done, 0 for not yet

    @Column(name = "response")
    String response;

    @Column(name = "postDate")
    Date postDate;

    @Column(name = "responseDate")
    Date responseDate;
}
