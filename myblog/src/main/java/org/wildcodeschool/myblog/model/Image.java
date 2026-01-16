package org.wildcodeschool.myblog.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String irl;

    @ManyToMany(mappedBy = "images")
    private List<Article> articles;

    // Getters & Setters

}
