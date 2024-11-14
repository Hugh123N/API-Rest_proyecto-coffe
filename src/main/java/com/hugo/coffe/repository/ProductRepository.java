package com.hugo.coffe.repository;

import com.hugo.coffe.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Modifying
    @Transactional
    @Query("update producto p set p.status=:status where p.id=:id")
    Integer updateStatus(@Param("status") String status,@Param("id") Integer id);

}
