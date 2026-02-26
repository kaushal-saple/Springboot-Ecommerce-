package com.ecommerce.sb_ecommerce.Service;

import com.ecommerce.sb_ecommerce.exception.ApiException;
import com.ecommerce.sb_ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecommerce.model.Category;
import com.ecommerce.sb_ecommerce.payload.CategoryDTO;
import com.ecommerce.sb_ecommerce.payload.CategoryResponse;
import com.ecommerce.sb_ecommerce.respository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
//    List<Category> categories = new ArrayList<>();

    @Autowired
    private ModelMapper modelMapper;



    //retrieve ALl categories
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();


        Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortByandOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()) {
            throw new ApiException("No categories have been created");
        }

        List<CategoryDTO> categoryDTO =  categories.stream()
                .map(category ->modelMapper.map(category , CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTO);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    //add new category
    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category categoryEntity = modelMapper.map(categoryDTO, Category.class);
        Category savedCategoryDB = categoryRepository.findByCategoryName(categoryEntity.getCategoryName());

        if (savedCategoryDB != null) {
            throw new ApiException("Category already exists with name: " + categoryDTO.getCategoryName());
        }

        Category savedCategory = categoryRepository.save(categoryEntity);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }


    //delete category
    @Override
    public CategoryDTO deleteCategory(Long categoryID) {
        Optional<Category>existingCategoryOptional = categoryRepository.findById(categoryID);
         Category existingCategory =  existingCategoryOptional
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryID",categoryID));

        categoryRepository.delete(existingCategory);
        return modelMapper.map(existingCategory, CategoryDTO.class);
    }

    //update category
    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryID) {
        Category categoryEntity = modelMapper.map(categoryDTO,Category.class);

        Optional<Category> existingCategoryOptional = categoryRepository.findById(categoryID);
        Category existingCategoryDB = existingCategoryOptional
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryID",categoryID));

        categoryEntity.setCategoryId(categoryID);
        Category savedCategory =  categoryRepository.save(categoryEntity);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
