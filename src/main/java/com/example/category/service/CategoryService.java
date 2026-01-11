package com.example.category.service;

import com.example.category.dto.CategoryDashboardDTO;
import com.example.category.entity.Category;
import com.example.category.repository.CategoryRepository;
import com.example.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    // ================= CREATE CATEGORY =================
    public Category createCategory(Category category) {
        // categoryQuantity comes from frontend (editable)
        return categoryRepository.save(category);
    }

    // ================= CATEGORY DASHBOARD =================
    public List<CategoryDashboardDTO> getCategoryDashboard() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> {
                    CategoryDashboardDTO dto = new CategoryDashboardDTO();

                    dto.setCategoryId(category.getCategoryId());
                    dto.setCategoryName(category.getCategoryName());
                    dto.setDescription(category.getDescription());
                    dto.setStatus(category.isStatus());

                    // Editable quantity (allowed)
                    dto.setCategoryQuantity(category.getCategoryQuantity());

                    // Actual used products (calculated)
                    dto.setProductCount(
                            productRepository.countActiveProductsByCategoryId(
                                    category.getCategoryId()
                            )
                    );

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ================= GET CATEGORY BY ID =================
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // ================= UPDATE CATEGORY =================
    public Category updateCategory(int id, Category updatedCategory) {
        Category existing = getCategoryById(id);

        if (existing != null) {
            existing.setCategoryName(updatedCategory.getCategoryName());
            existing.setDescription(updatedCategory.getDescription());

            // ✅ UPDATE EDITABLE QUANTITY
            existing.setCategoryQuantity(updatedCategory.getCategoryQuantity());

            return categoryRepository.save(existing);
        }

        return null;
    }

    // ================= SOFT DELETE CATEGORY =================
    public String deactivateCategory(int id) {
        Category category = getCategoryById(id);

        if (category == null) {
            return "Category not found";
        }

        long usedProducts =
                productRepository.countActiveProductsByCategoryId(id);

        if (usedProducts > 0) {
            return "Cannot deactivate category. Assign products first.";
        }

        category.setStatus(false);
        categoryRepository.save(category);

        return "Category deactivated successfully";
    }
    
 // Activate Category
    public String activateCategory(int id) {
        Category category = getCategoryById(id);

        if (category == null) {
            return "Category not found";
        }

        category.setStatus(true);
        categoryRepository.save(category);

        return "Category activated successfully";
    }

}
