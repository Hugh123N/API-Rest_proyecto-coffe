package com.hugo.coffe.service.serviceImpl;

import com.hugo.coffe.JWT.JwtFilter;
import com.hugo.coffe.constens.CoffeConstans;
import com.hugo.coffe.model.Category;
import com.hugo.coffe.model.Product;
import com.hugo.coffe.repository.ProductRepository;
import com.hugo.coffe.service.ProductService;
import com.hugo.coffe.utils.CoffeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap,false)){
                    productRepository.save(getProductFromMap(requestMap,false));
                    return CoffeUtils.getResponseEntity("Producto registrado correctamente",HttpStatus.OK);
                }
                return CoffeUtils.getResponseEntity(CoffeConstans.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }
            return CoffeUtils.getResponseEntity(CoffeConstans.UNAAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CoffeUtils.getResponseEntity(CoffeConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // Método para validar los datos del producto en el mapa de solicitud
    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        // Verifica si el mapa contiene la clave "name" (necesaria tanto para agregar como actualizar).
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                //para actualizar producto si "name" e "id" están presentes y validateId es true.
                return true;
            }else if(!validateId){
                // para agregar un nuevo producto
                return true;
            }
        }
        // Devuelve false si falta "name" o "id" según los requisitos de la operación.
        return false;
    }
    // Método crear Product a partir de los datos en el mapa de solicitud.
    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category=new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        Product product=new Product();
        // Si se está agregando un nuevo producto (isAdd == false)
        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));
        }else{
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Double.parseDouble(requestMap.get("price")));
        return product;
    }

}
