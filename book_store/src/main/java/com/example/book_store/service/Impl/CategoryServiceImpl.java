package com.example.book_store.service.Impl;

import com.example.book_store.model.Category;
import com.example.book_store.model.exceptions.CategoryNotFoundException;
import com.example.book_store.repository.CategoryRepository;
import com.example.book_store.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository cr;

    public CategoryServiceImpl(CategoryRepository cr) {
        this.cr = cr;
    }

    @Override
    public List<Category> findAll() {
        return this.cr.findAll();
    }

    @Override
    public List<Category> findAllByName(String name) {
        return this.cr.findAllByName(name);
    }

    @Override
    public Category findById(Long id) {
        return this.cr.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category save(Category category) {
        return this.cr.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        Category c = this.findById(id);
        c.setName(category.getName());
        return this.cr.save(c);
    }

    @Override
    public Category updateName(Long id, String name) {
        Category c = this.findById(id);
        c.setName(name);
        return this.cr.save(c);
    }

    @Override
    public void deleteById(Long id) {
        this.cr.deleteById(id);
    }

//    @PostConstruct
//    public void init(){
//        Category category = new Category("category1");
//        this.cr.save(category);
//    }
}
