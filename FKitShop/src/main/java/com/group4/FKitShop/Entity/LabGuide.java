package com.group4.FKitShop.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

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
    @NotNull
    String description;
    @NotNull
    int step;
    @NotNull
    String content;
    @ManyToOne
    @JoinColumn(name = "labID")
    @JsonBackReference
    Lab lab;

}
