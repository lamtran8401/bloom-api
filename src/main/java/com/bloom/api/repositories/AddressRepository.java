package com.bloom.api.repositories;

import com.bloom.api.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findAddressesByUserId(Integer userId);

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId and a.isDefault = true")
    void updateAllDefaultAddressByUserId(@Param("userId") Integer userId);

    Address findByIdAndUserId(Integer addressId, Integer userId);
}