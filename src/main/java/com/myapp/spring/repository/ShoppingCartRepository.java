package com.myapp.spring.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.spring.model.ShoppingCart;
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {

public List<ShoppingCart> findByUserName(String userName);

}
