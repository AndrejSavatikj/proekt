package com.example.book_store.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Min(value = 0, message = "Price must be bigger than 2")
    private Float price;

    @Min(value = 0, message = "Can't have negative quantity")
    private Integer quantity;

    @ManyToOne
    private Category category;

    @Column(name = "image")
    @Lob
    private String image64Base;

    @ManyToMany
    @JoinTable(name = "authors_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors;

    public Book(){
        authors = new ArrayList<>();
    }

    public Book(@NotNull String name, @NotNull @Min(value = 0, message = "Price must be bigger than 2") Float price, @Min(value = 0, message = "Can't have negative quantity") Integer quantity, Category category, String image64Base, List<Author> authors) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.image64Base = image64Base;
        this.authors = authors;
    }

    public Book(@NotNull String name, @NotNull @Min(value = 0, message = "Price must be bigger than 2") Float price, @Min(value = 0, message = "Can't have negative quantity") Integer quantity, Category category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImage64Base() {
        return image64Base;
    }

    public void setImage64Base(String image64base) {
        this.image64Base = image64base;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
