package com.hugo.coffe.rest.restImpl;

import com.hugo.coffe.rest.UserRest;
import com.hugo.coffe.service.UserService;
import com.hugo.coffe.utils.CoffeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.hugo.coffe.constens.CoffeConstans.SOMETHING_WENT_WRONG;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;


    @Override
    public ResponseEntity<String> singUp(Map<String, String> requestMap) {
        try {
            return userService.singUp(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CoffeUtils.getResponseEntity(SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
