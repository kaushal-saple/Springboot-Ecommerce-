package com.ecommerce.sb_ecommerce.controller;


import com.ecommerce.sb_ecommerce.Service.CategoryService;
import com.ecommerce.sb_ecommerce.model.Category;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public ResponseEntity<List<Category>>getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<String> addCategory(@Valid @RequestBody Category category){
        String newCategory = categoryService.addCategory(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/categories/{categoryID}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryID){
            String status = categoryService.deleteCategory(categoryID);
            return  new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PutMapping("/api/admin/categories/{categoryID}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryID){
           Category savedCategory = categoryService.updateCategory(category,categoryID);
           return new ResponseEntity<>("Updated Successfully CategoryID: " + categoryID,HttpStatus.CREATED);

    }




}
