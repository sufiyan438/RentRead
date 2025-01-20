package com.RentRead.Rent_Read_System.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RentRead.Rent_Read_System.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
}
