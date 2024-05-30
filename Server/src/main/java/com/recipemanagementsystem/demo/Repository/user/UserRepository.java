package com.recipemanagementsystem.demo.Repository.user;

import com.recipemanagementsystem.demo.Entity.User;
import com.recipemanagementsystem.demo.Enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    User findByRole(Role role);
}
