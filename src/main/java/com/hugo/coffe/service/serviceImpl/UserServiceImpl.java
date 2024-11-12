package com.hugo.coffe.service.serviceImpl;

import com.google.common.base.Strings;
import com.hugo.coffe.JWT.JwtFilter;
import com.hugo.coffe.JWT.JwtUtil;
import com.hugo.coffe.JWT.UserDetailsServiceImpl;
import com.hugo.coffe.constens.CoffeConstans;
import com.hugo.coffe.model.User;
import com.hugo.coffe.repository.UserRepository;
import com.hugo.coffe.service.UserService;
import com.hugo.coffe.utils.CoffeUtils;
import com.hugo.coffe.utils.EmailUtils;
import com.hugo.coffe.wraper.UserWraper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.*;

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
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    EmailUtils emailUtils;
    @Autowired
    ModelMapper mapper;
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
    /***********************  LISTA USER  ***************/
    @Override
    public ResponseEntity<List<UserWraper>> getAllUser() {
        try {
            if(jwtFilter.isAdmin()){
                List<UserWraper> list=new ArrayList<>();
                List<User> listUser=userRepository.findAll();
                for(User user: listUser){
                    if(user.getRole().equals("user")){
                        UserWraper userWraper=new UserWraper();
                        mapper.map(user,userWraper);
                        list.add(userWraper);
                    }
                }
                return new ResponseEntity<>(list,HttpStatus.OK);
            }else
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /***********************  UPDATE USER  STATUS***************/
    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                Optional<User> optional=userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if(optional.isPresent()){
                    userRepository.updateStatus(Integer.parseInt(requestMap.get("id")),requestMap.get("status"));
                    //lista de admins por su email
                    sendMailAllAdmin(requestMap.get("status"), optional.get().getEmail(),userRepository.getAllAdmin());
                    return CoffeUtils.getResponseEntity("Estado de Usuario actualizado con exito",HttpStatus.OK);
                }else
                    return CoffeUtils.getResponseEntity("El ID de Usuario no existe.",HttpStatus.OK);
            }else
                return CoffeUtils.getResponseEntity(CoffeConstans.UNAAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CoffeUtils.getResponseEntity(CoffeConstans.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sentSimpleMessage(jwtFilter.getCurrentUser(),"Cuenta aprobada", "USER:- "+user+"\n Esta aprobada por \n ADMIN:- "+jwtFilter.getCurrentUser(), allAdmin);
        }else
            emailUtils.sentSimpleMessage(jwtFilter.getCurrentUser(),"cuenta deshabilitada", "USER:- "+user+"\n Esta desabilitada por \n ADMIN:- "+jwtFilter.getCurrentUser(), allAdmin);
    }

    /***********************  UPDATE USER  PASSWORD***************/
    @Override
    public ResponseEntity<String> checkToken() {
        return CoffeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> cambioPassword(Map<String,String> requestMap) {
       try {
            Optional<User> userOptional=userRepository.findByEmail(jwtFilter.getCurrentUser());
            if(userOptional.isPresent()){
                User user=userOptional.get();
               if(user.getPassword().equals(requestMap.get("oldPassword"))){
                    user.setPassword(requestMap.get("newPassword"));
                    userRepository.save(user);
                   return CoffeUtils.getResponseEntity("La contraseña se cambio correctamente.",HttpStatus.OK);
               }
                return CoffeUtils.getResponseEntity("Password incorrecto.", HttpStatus.BAD_REQUEST);
            }
            return CoffeUtils.getResponseEntity(CoffeConstans.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
       }catch (Exception e){
           e.printStackTrace();
       }
       return CoffeUtils.getResponseEntity(CoffeConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*********************** OLVIDO PASSWORD / ENVIA CORREO AL USUARIO QUE SOLICITA ***************/
    @Override
    public ResponseEntity<String> olvidoPassword(Map<String, String> requestMap) {
        try {
            Optional<User> userOptional=userRepository.findByEmail(requestMap.get("email"));
            if(userOptional.isPresent() && !Strings.isNullOrEmpty(userOptional.get().getEmail())){
                emailUtils.olvidoEmail(userOptional.get().getEmail(),"Credencial por sistema de gestion de cafe",userOptional.get().getPassword());
            }
            return CoffeUtils.getResponseEntity("Revisa tu correo para ver las credenciales.", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
       return CoffeUtils.getResponseEntity(CoffeConstans.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
