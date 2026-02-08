package com.ecommerce.sb_ecommerce.Service;

import com.ecommerce.sb_ecommerce.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    List<Category> categories = new ArrayList<>();

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public String addCategory(Category category) {
        categories.add(category);
        return "Successfully added Category";
    }
}
