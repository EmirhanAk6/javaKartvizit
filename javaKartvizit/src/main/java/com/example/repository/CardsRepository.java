package com.example.repository;

import com.example.model.CardsModel;
import com.example.model.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardsRepository extends JpaRepository<CardsModel, Integer> {
    
    List<CardsModel> findByUserOrderByIdDesc(UsersModel user);
    long countByUser(UsersModel user);
    
    // Düzeltilmiş arama query'si
    @Query("SELECT c FROM CardsModel c WHERE " +
           "(LOWER(c.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.jobTitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<CardsModel> searchPublicCards(@Param("searchTerm") String searchTerm);
}