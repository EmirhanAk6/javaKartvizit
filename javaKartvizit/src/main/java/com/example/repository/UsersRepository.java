package com.example.repository;

import com.example.model.UsersModel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersModel, Integer> {

	
	
	
	Optional<UsersModel> findByUsername(String username);
	Optional<UsersModel> findByEmail(String email);
	
	boolean existsByUsername (String username);
	boolean existsByEmail(String email);
}
