package com.hugo.coffe.service.serviceImpl;

import com.google.common.base.Strings;
import com.hugo.coffe.JWT.JwtFilter;
import com.hugo.coffe.constens.CoffeConstans;
import com.hugo.coffe.model.Category;
import com.hugo.coffe.repository.CategoryRepository;
import com.hugo.coffe.service.CategoryService;
import com.hugo.coffe.utils.CoffeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j //para uso de log priueba para git hub
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    JwtFilter jwtFilter;

    /***********************  AGREGAR NUEVO CATEGORIA (SOLO ADMIN) ***************/
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

    /***********************  LISTADO CATEGORIA ***************/
    @Override
    public ResponseEntity<List<Category>> findAll(String filterValue) {
        try {
            if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                log.info("Interno si ");
                return new ResponseEntity<List<Category>>(categoryRepository.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /***********************  ACTUALIZAR CATEGORIA ***************/
    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if(validateCategoryMap(requestMap,true)){
                    Optional<Category> optional= categoryRepository.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty()){
                        categoryRepository.save(getCategoryFromMap(requestMap,true));
                        return CoffeUtils.getResponseEntity("Categoria actualizado correctamente",HttpStatus.OK);
                    }else{
                        CoffeUtils.getResponseEntity("ID de la Categoria no existe.",HttpStatus.OK);
                    }
                }
                return CoffeUtils.getResponseEntity(CoffeConstans.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }else{
                return CoffeUtils.getResponseEntity(CoffeConstans.UNAAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return CoffeUtils.getResponseEntity(CoffeConstans.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
