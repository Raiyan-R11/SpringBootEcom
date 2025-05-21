package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIException("No categories created yet!");
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null) {
            throw new APIException("Category with name "+category.getCategoryName()+" already exists!");
        }
        //category.setCategoryId(nextId++);
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {

        // ---v3: custom exception (ResourceNotFoundException)
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(category);
        return "Category with categoryId: " + categoryId + " was deleted";

        // ---v2
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        categoryRepository.delete(category);
//        return "Category with categoryId: " + categoryId + " was deleted";

        // ---v1
//        List<Category> categories = categoryRepository.findAll();
//
//        Category category = categories.stream()
//                .filter(c->c.getCategoryId().equals(categoryId))
//                .findFirst()
//                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        categoryRepository.delete(category);
//
//        if(category==null){
//            return "Category not found";
//        }
//        return "Category with categoryId " +categoryId+" deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        // ---v3: custom exception (ResourceNotFoundException)
        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);

        Category savedCategory = savedCategoryOptional
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return savedCategory;

        // ---v2
//        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);
//
//        Category savedCategory = savedCategoryOptional
//                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with categoryId: " + categoryId + " not found"));
//        category.setCategoryId(categoryId);
//        savedCategory = categoryRepository.save(category);
//        return savedCategory;

        // ---v1
//        List<Category> categories = categoryRepository.findAll();
//        Optional<Category> existingCategory = categories.stream().
//                filter((c) -> c.getCategoryId().equals(categoryId))
//                .findFirst();
//
//        if(existingCategory.isPresent()){
//            Category updatedExistingCategory = existingCategory.get();
//            updatedExistingCategory.setCategoryName(category.getCategoryName());
//            Category updatedCategory = categoryRepository.save(updatedExistingCategory);
//            return updatedCategory;
//        }else{
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
//        }
    }
}
