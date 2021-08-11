package com.myapp.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.spring.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

//	List<Product> findAll();
	Optional<List<Product>> findByPriceGreaterThanEqual(Double price);
	
	Optional<List<Product>> findByProductName(String productName);
//	List<Product> findProductByName();
	Optional<List<Product>> findByProductNameOrPrice(String productName, Double price);
	

}

