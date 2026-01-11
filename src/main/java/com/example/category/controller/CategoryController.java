package com.example.category.controller;

import com.example.category.dto.CategoryDashboardDTO;
import com.example.category.entity.Category;
import com.example.category.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping("/dashboard")
    public List<CategoryDashboardDTO> getDashboard() {
        return categoryService.getCategoryDashboard();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable int id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable int id,
                                   @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }
    
    @PutMapping("/activate/{id}")
    public String activateCategory(@PathVariable int id) {
        return categoryService.activateCategory(id);
    }


    @DeleteMapping("/{id}")
    public String deactivateCategory(@PathVariable int id) {
        return categoryService.deactivateCategory(id);
    }
}
