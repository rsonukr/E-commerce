package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{
	
	Category findByCategoryName(String categoryname);
	
	

}
