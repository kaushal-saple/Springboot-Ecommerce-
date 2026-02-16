package com.ecommerce.sb_ecommerce.Service;

import com.ecommerce.sb_ecommerce.model.Category;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    String addCategory(Category category);

    String deleteCategory(Long categoryID);

    Category updateCategory(Category category, Long categoryID);


}