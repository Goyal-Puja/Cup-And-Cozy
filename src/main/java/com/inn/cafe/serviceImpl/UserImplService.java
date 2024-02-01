package com.inn.cafe.serviceImpl;

import com.inn.cafe.bean.UserBean;
import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.jwt.CustomerUserDetailsService;
import com.inn.cafe.jwt.JwtFilter;
import com.inn.cafe.jwt.JwtUtil;
import com.inn.cafe.model.User;
import com.inn.cafe.repository.UserRepository;
import com.inn.cafe.service.UserService;
import com.inn.cafe.util.CafeUtil;
import com.inn.cafe.util.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mysql.cj.util.StringUtils.isNullOrEmpty;

@Slf4j
@Service
public class UserImplService implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    EmailUtils emailUtils;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
          log.info("Inside Signup {}", requestMap);
          try {
              if (validateSignUpMap(requestMap)) {
                  User user = userRepository.findByEmailId(requestMap.get("email"));
                  if (Objects.isNull(user)) {
                      userRepository.save(getUserFromMap(requestMap));
                      return CafeUtil.getResponseEntity("Successfully Registered", HttpStatus.OK);
                  } else {
                      return CafeUtil.getResponseEntity("Email already exist", HttpStatus.BAD_REQUEST);
                  }
              } else {
                  return CafeUtil.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
              }
          } catch(Exception ex){
              ex.printStackTrace();
          }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private boolean validateSignUpMap(Map<String, String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        } else {
            return false;
        }
    }
    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside leogin");
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
            );
            if(auth.isAuthenticated()){
                if(customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+ jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                            customerUserDetailsService.getUserDetail().getRole())+"\"}",HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\""+"wait for admin approval."+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex){
            log.error("{}", ex);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials.."+"\"}",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserBean>> getAllUser() {
        try {
            if(jwtFilter.isAdmin()){
                  return new ResponseEntity<>(userRepository.getAllUser(),HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                   Optional<User> optional = userRepository.findById(Integer.parseInt(requestMap.get("id")));
                   if(optional.isPresent()){
                       userRepository.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                  //     sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userRepository.getAllAdmin());
                       return CafeUtil.getResponseEntity("User status updated successfully", HttpStatus.OK);
                   } else {
                       return CafeUtil.getResponseEntity("User Id does not Exist",HttpStatus.OK);
                   }
            } else {
                return CafeUtil.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
       return CafeUtil.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
           User userObj = userRepository.findByEmail(jwtFilter.getCurrentUser());
           if(!userObj.equals(null)){
               if(userObj.getPassword().equals(requestMap.get("oldPassword"))){
                   userObj.setPassword(requestMap.get("newPassword"));
                   userRepository.save(userObj);
                   return CafeUtil.getResponseEntity("Password Updated Successfully",HttpStatus.OK);
               }
               return CafeUtil.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
           }
           return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepository.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user) && !isNullOrEmpty(user.getEmail()))
                   emailUtils.forgotMail(user.getEmail(),"Credentials by Cafe Management System",user.getPassword());
                return CafeUtil.getResponseEntity("Check your mail for Credentials",HttpStatus.OK);

        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status != null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved","USER:- "+user+" \n is approved by \nADMIN:-"+ jwtFilter.getCurrentUser() , allAdmin );
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled","USER:- "+user+" \n is disabled by \nADMIN:-"+ jwtFilter.getCurrentUser() , allAdmin );
        }
    }
}
