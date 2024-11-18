package com.hugo.coffe.repository;

import com.hugo.coffe.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {

    @Query("select f from Factura f where f.createdBy=:userName order by f.id desc")
    List<Factura> getFacturaByUserName(@Param("userName") String userName);

}
