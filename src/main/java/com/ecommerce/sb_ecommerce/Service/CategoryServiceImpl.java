package com.ecommerce.sb_ecommerce.Service;

import com.ecommerce.sb_ecommerce.model.Category;
import com.ecommerce.sb_ecommerce.respository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        return categoryRepository.findAll();
    }

    //add new category
    @Override
    public String addCategory(Category category) {
        categoryRepository.save(category);
        return "Successfully added Category";
    }


    //delete category
    @Override
    public String deleteCategory(Long categoryID) {
        Optional<Category>existingCategoryOptional = categoryRepository.findById(categoryID);
         Category existingCategory =  existingCategoryOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));

        categoryRepository.delete(existingCategory);
        return "Deleted CategoryID: " + categoryID +" Successfully";
    }

    //update category
    @Override
    public Category updateCategory(Category category, Long categoryID) {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(categoryID);
        Category existingCategory = existingCategoryOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
        category.setCategoryID(categoryID);
        return categoryRepository.save(category);

    }
}
