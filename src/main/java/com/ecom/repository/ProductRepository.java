package com.ecom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecom.model.Product;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByIsActiveTrue();
    
    Page<Product> findByIsActiveTrue(Pageable pageable);

    List<Product> findByCategory(String category);
    
    List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch1, String ch2);
    
    Page<Product> findByCategoryAndIsActiveTrue(String category, Pageable pageable);

}
