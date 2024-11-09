package com.hugo.coffe.JWT;

import com.hugo.coffe.model.User;
import com.hugo.coffe.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    private User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername. {}", username);
        Optional<User> userOptional=userRepository.findByEmail(username);
        if(!Objects.isNull(userOptional)) {
            userDetail=userOptional.get();
            return new org.springframework.security.core.userdetails.User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());

        }else{
            throw new UsernameNotFoundException("user not found.");
        }
    }

    public User getUserDetails(){
        //User user=userDetail;
        //user.setPassword(null);
        return userDetail;
    }


}
