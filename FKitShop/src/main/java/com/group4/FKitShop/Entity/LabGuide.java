package com.group4.FKitShop.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class LabGuide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int labGuideID;
    @NotNull
    @Length(max = 30, message = "LabID length is not more than 30 characters")
    String labID;
    @NotNull
    String stepDescription;
    @NotNull
    int stepNumber;
    String stepImage;
}
