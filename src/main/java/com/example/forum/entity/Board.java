package com.example.forum.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "boards")
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = true, nullable = false, length = 20)
    private String slug;

    @Column(length = 255)
    private String description;
}
