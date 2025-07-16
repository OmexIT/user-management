package com.example.usermanagement.merchant.repository;

import com.example.usermanagement.merchant.model.Merchant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends CrudRepository<Merchant, Long> {

	Optional<Merchant> findByMerchantCode(String merchantCode);

	boolean existsByMerchantCode(String merchantCode);
}
