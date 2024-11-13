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

}
