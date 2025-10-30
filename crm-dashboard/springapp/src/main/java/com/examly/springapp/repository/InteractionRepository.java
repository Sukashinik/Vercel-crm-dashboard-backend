package com.examly.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.examly.springapp.model.Interaction;
import com.examly.springapp.model.User;
import com.examly.springapp.model.Customer;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    List<Interaction> findByCustomer(Customer customer);
    List<Interaction> findByUser(User user);
    List<Interaction> findTop10ByOrderByIdDesc();
    
    @Modifying
    @Query("UPDATE Interaction i SET i.user = null WHERE i.user.id = :userId")
    void setUserToNullByUserId(@Param("userId") Long userId);
}