package com.hugo.coffe.service.serviceImpl;

import com.hugo.coffe.JWT.JwtFilter;
import com.hugo.coffe.constens.CoffeConstans;
import com.hugo.coffe.model.Category;
import com.hugo.coffe.repository.CategoryRepository;
import com.hugo.coffe.service.CategoryService;
import com.hugo.coffe.utils.CoffeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if(validateCategoryMap(requestMap, false)){
                    categoryRepository.save(getCategoryFromMap(requestMap,false));
                    return CoffeUtils.getResponseEntity("Categoria agregado correctamente", HttpStatus.OK);
                }
            }else {
                return CoffeUtils.getResponseEntity(CoffeConstans.UNAAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return CoffeUtils.getResponseEntity(CoffeConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if (!validateId ) {
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String,String> requestMap, boolean idAdd){
        Category category=new Category();
        if(idAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }

}
