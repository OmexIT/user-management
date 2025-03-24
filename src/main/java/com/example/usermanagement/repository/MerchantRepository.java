package com.example.usermanagement.repository;

import com.example.usermanagement.model.Merchant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends CrudRepository<Merchant, Long> {

	Optional<Merchant> findByMerchantCode(String merchantCode);

	boolean existsByMerchantCode(String merchantCode);
}
