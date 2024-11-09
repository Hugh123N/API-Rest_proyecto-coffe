package com.hugo.coffe.service.serviceImpl;

import com.hugo.coffe.JWT.JwtFilter;
import com.hugo.coffe.JWT.JwtUtil;
import com.hugo.coffe.JWT.UserDetailsServiceImpl;
import com.hugo.coffe.constens.CoffeConstans;
import com.hugo.coffe.model.User;
import com.hugo.coffe.repository.UserRepository;
import com.hugo.coffe.service.UserService;
import com.hugo.coffe.utils.CoffeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager autenthAuthenticationManager;
    @Autowired
    UserDetailsServiceImpl detailService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    /*********************** SING UP **********************************/
    @Override
    public ResponseEntity<String> singUp(Map<String, String> requestMap) {
        log.info("Inscripcion interna {}", requestMap);
        try {
        if(validateSingUp(requestMap)){
            Optional<User> optional=userRepository.findByEmail(requestMap.get("email"));
            if(optional.isPresent())
                return CoffeUtils.getResponseEntity("El correo electrónico ya existe", HttpStatus.BAD_REQUEST);
            else{
                userRepository.save(getUserFromMap(requestMap));
                return CoffeUtils.getResponseEntity("Registro exitoso.", HttpStatus.OK);
            }
        }else{
            return CoffeUtils.getResponseEntity(CoffeConstans.INVALID_DATA, HttpStatus.BAD_REQUEST);//error 400
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  CoffeUtils.getResponseEntity(CoffeConstans.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSingUp(Map<String,String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
          && requestMap.containsKey("email") && requestMap.containsKey("password"))
            return true;
        else
            return false;
    }
    private User getUserFromMap(Map<String,String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
    /***********************  LOG IN  ***************/
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Login Interna {}");
        try {
            Authentication auth=autenthAuthenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password")));
            if(auth.isAuthenticated()){
                if(detailService.getUserDetails().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("\"token\":\""+jwtUtil.generateToken(
                            detailService.getUserDetails().getEmail(),
                            detailService.getUserDetails().getRole())+"\"}",
                            HttpStatus.OK);
                }else{
                   return new ResponseEntity<String>("{\"message\":\""+"Esperar que se agregure la aprobacion de Admin."+"\"}"
                           ,HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception e){
            log.error("{}",e);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Malas credenciales."+"\"}"
                ,HttpStatus.BAD_REQUEST);
    }
}
