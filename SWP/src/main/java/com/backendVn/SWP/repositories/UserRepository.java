package com.backendVn.SWP.repositories;

import com.backendVn.SWP.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByUserName(String userName);

  boolean existsByUserName(String userName);

  List<User> findByTitle(String role);
}