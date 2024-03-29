package com.inn.cafe.restImpl;

import com.inn.cafe.bean.UserBean;
import com.inn.cafe.util.CafeUtil;
import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.rest.UserRest;
import com.inn.cafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{
              return userService.signUp(requestMap);
            //   return new ResponseEntity<>("Successfully registered",HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        //return new ResponseEntity<String>("{\"message\":\"Something went Wrong}", HttpStatus.INTERNAL_SERVER_ERROR);
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserBean>> getAllUser() {
        try {
            return userService.getAllUser();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UserBean>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            return userService.update(requestMap);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try{
            return userService.checkToken();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            return userService.changePassword(requestMap);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            return userService.forgotPassword(requestMap);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
