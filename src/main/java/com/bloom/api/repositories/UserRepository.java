package com.bloom.api.repositories;

import com.bloom.api.models.Address;
import com.bloom.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId")
    List<Address> findAllAddressByUserId(Integer userId);
}