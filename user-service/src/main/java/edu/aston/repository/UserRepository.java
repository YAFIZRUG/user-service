package edu.aston.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.aston.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
