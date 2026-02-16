package com.ecommerce.sb_ecommerce.Service;

import com.ecommerce.sb_ecommerce.exception.ApiException;
import com.ecommerce.sb_ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecommerce.model.Category;
import com.ecommerce.sb_ecommerce.respository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
//    List<Category> categories = new ArrayList<>();


    //retrieve ALl categories
    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new ApiException("No categories have been created");
        }
        return categories;
    }

    //add new category
    @Override
    public String addCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory != null) {
            throw new ApiException("Category already exists with name: " + category.getCategoryName());
        }
        categoryRepository.save(category);
        return "Successfully added Category";
    }


    //delete category
    @Override
    public String deleteCategory(Long categoryID) {
        Optional<Category>existingCategoryOptional = categoryRepository.findById(categoryID);
         Category existingCategory =  existingCategoryOptional
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryID",categoryID));

        categoryRepository.delete(existingCategory);
        return "Deleted CategoryID: " + categoryID +" Successfully";
    }

    //update category
    @Override
    public Category updateCategory(Category category, Long categoryID) {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(categoryID);
        Category existingCategory = existingCategoryOptional
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryID",categoryID));
        category.setCategoryID(categoryID);
        return categoryRepository.save(category);
    }
}
