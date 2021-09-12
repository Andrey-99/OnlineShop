package com.ibs21.OnlineShop.repos;

import com.ibs21.OnlineShop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByProducttitle(String producttitle, Pageable pageable);
    Page<Product> findById(Long id, Pageable pageable);


}
