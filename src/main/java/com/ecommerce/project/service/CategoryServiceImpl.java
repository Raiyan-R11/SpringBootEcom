package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private List<Category> categories = new ArrayList<>();

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        categories.add(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {

        Category category = categories.stream()
                .filter(c->c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        categories.remove(category);

        if(category==null){
            return "Category not found";
        }
        return "Category with categoryId " +categoryId+" deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> existingCategory = categories.stream().
                filter((c) -> c.getCategoryId().equals(categoryId))
                .findFirst();

        if(existingCategory.isPresent()){
            Category updatedExistingCategory = existingCategory.get();
            updatedExistingCategory.setCategoryName(category.getCategoryName());
            return updatedExistingCategory;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
        }
    }
}
