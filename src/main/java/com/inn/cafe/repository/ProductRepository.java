package com.inn.cafe.repository;

import com.inn.cafe.bean.ProductBean;
import com.inn.cafe.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<ProductBean> getAllProduct();

    @Transactional
    @Modifying
    Integer updateProductStatus(@Param("status") String status, @Param("id") Integer id);

    List<ProductBean> getProductByCategory(@Param("id") Integer id);

    ProductBean getProductById(@Param("id") Integer id);
}
