package com.hugo.coffe.rest.restImpl;

import com.hugo.coffe.rest.UserRest;
import com.hugo.coffe.service.UserService;
import com.hugo.coffe.utils.CoffeUtils;
import com.hugo.coffe.wraper.UserWraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try{
            return userService.login(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CoffeUtils.getResponseEntity(SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWraper>> getAllUser() {
        try {
            return userService.getAllUser();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  new ResponseEntity<List<UserWraper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
