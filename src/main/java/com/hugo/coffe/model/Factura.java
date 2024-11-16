package com.hugo.coffe.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Data //Getter and setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "factura")
public class Factura implements Serializable {

    private static final long serialVersionIUD=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uuid;
    private String name;
    private String email;
    private String contactNumber;
    private String metodoPago;
    private Double total;
    @Column(columnDefinition = "nvarchar(max)")
    private String productDetail;
    private String createdBy;


}
