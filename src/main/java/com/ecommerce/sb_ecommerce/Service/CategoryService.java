package com.ecommerce.sb_ecommerce.Service;

import com.ecommerce.sb_ecommerce.model.Category;
import com.ecommerce.sb_ecommerce.payload.CategoryDTO;
import com.ecommerce.sb_ecommerce.payload.CategoryResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer page, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO addCategory(CategoryDTO category);

    CategoryDTO deleteCategory(Long categoryID);

    CategoryDTO updateCategory(CategoryDTO category, Long categoryID);


}