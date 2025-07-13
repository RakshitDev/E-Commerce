package com.ecommerce.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Roles;
@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

	Optional<Roles> findByRoleName(AppRole roleUser);
	 
	

}
