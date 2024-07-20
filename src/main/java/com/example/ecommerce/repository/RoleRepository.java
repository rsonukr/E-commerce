package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

}
