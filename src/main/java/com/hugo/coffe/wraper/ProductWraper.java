package com.hugo.coffe.wraper;

import com.hugo.coffe.model.Category;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data //getter y setter
public class ProductWraper {

    Integer id;
     String name;
     String description;
     Double price;
     String status;
     Integer categoryId;
     String categoryName;

     public ProductWraper(Integer id, String name){
      this.id=id;
      this.name=name;
     }

     public ProductWraper(Integer id,String name, String description, Double price){
      this.id=id;
      this.name=name;
      this.description=description;
      this.price=price;
     }

}
