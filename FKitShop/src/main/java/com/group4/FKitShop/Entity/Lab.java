package com.group4.FKitShop.Entity;

import jakarta.persistence.*;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Lab {
    @Id
    @NotNull
    String labID;

    @NotNull
    @Column(length = 30)
    String productID;

    @NotNull
    @Column(length = 100)
    String name;

    String description;

    @Length(max = 6, message = "Level length is maximum of 6 characters !!")
    String level;

    @NotNull
    Date createDate;

    String fileNamePDF;
}
