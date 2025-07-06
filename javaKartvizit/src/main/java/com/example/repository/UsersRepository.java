package com.example.repository;

import com.example.model.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersModel, Integer> {

}
