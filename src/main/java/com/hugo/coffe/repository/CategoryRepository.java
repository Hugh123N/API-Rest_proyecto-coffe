package com.hugo.coffe.repository;

import com.hugo.coffe.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    //lista de categoria por producto en estado activo
    @Query("SELECT c FROM Category c WHERE c.id IN (SELECT p.category.id FROM producto p WHERE p.status = 'true')")
    List<Category> getAllCategory();

}
