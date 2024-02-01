package com.inn.cafe.serviceImpl;

import com.inn.cafe.bean.ProductBean;
import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.jwt.JwtFilter;
import com.inn.cafe.model.Category;
import com.inn.cafe.model.Product;
import com.inn.cafe.repository.ProductRepository;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.util.CafeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addProduct(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap, false)){
                    productRepository.save(getProductFromMap(requestMap, false));
                    return CafeUtil.getResponseEntity("Product added successfully", HttpStatus.OK);
                }
                return CafeUtil.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
            } else{
                return CafeUtil.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        Product product = new Product();
        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));
        } else{
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));

        return product;
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if (requestMap.containsKey("id") && validateId){
                return true;
            } else if(!validateId){
                return true;
            }
        }
        return false;
    }
    @Override
    public ResponseEntity<List<ProductBean>> getAllProduct() {
        try {
            return new ResponseEntity<>(productRepository.getAllProduct(), HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap,true)){
                 Optional<Product> optional = productRepository.findById(Integer.parseInt(requestMap.get("id")));
                 if(optional.isPresent()){
                     Product product = getProductFromMap(requestMap, true);
                     product.setStatus(optional.get().getStatus());
                     productRepository.save(product);
                     return CafeUtil.getResponseEntity("Product Updated Successfully",HttpStatus.OK);
                 } else {
                     return CafeUtil.getResponseEntity("Product Id does not exist",HttpStatus.OK);
                 }

                } else {
                    return CafeUtil.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<String> deleteProduct(Integer id) {
        try{
            if(jwtFilter.isAdmin()){
                Optional optional = productRepository.findById(id);
                if(optional.isPresent()){
                    productRepository.deleteById(id);
                    return CafeUtil.getResponseEntity("Product deleted successfully", HttpStatus.OK);
                } else {
                    return CafeUtil.getResponseEntity("Product Id does not exist",HttpStatus.OK);
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
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
              Optional optional =  productRepository.findById(Integer.parseInt(requestMap.get("id")));
              if(optional.isPresent()){
                  productRepository.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                  return CafeUtil.getResponseEntity("Product Status Updated Successfully", HttpStatus.OK);
              } else {
                  return CafeUtil.getResponseEntity("Product Id does not exist", HttpStatus.OK);
              }
            } else{
                return CafeUtil.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductBean>> getByCategory(Integer id) {
        try{
            return new ResponseEntity<>(productRepository.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductBean> getProductById(Integer id) {
        try {
            return new ResponseEntity<>(productRepository.getProductById(id), HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductBean(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
