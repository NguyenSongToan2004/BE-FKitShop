package com.group4.FKitShop.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data // @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "Tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagID")
    private int tagID;

    @Column(name = "tagName")
    private String tagName;

    @Column(name = "description")
    private String description;


}
