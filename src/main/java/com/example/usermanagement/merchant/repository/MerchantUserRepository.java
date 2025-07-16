package com.example.usermanagement.merchant.repository;

import com.example.usermanagement.merchant.model.MerchantUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantUserRepository extends CrudRepository<MerchantUser, Long> {

	Iterable<MerchantUser> findByUserId(Long userId);

	Iterable<MerchantUser> findByUserIdAndIsActiveTrue(Long userId);

	Iterable<MerchantUser> findByMerchantId(Long merchantId);

	Iterable<MerchantUser> findByMerchantIdAndIsActiveTrue(Long merchantId);

	Iterable<MerchantUser> findByBranchId(Long branchId);

	Iterable<MerchantUser> findByBranchIdAndIsActiveTrue(Long branchId);

	Iterable<MerchantUser> findByUserIdAndMerchantId(Long userId, Long merchantId);

	Iterable<MerchantUser> findByUserIdAndMerchantIdAndIsActiveTrue(Long userId, Long merchantId);

	Iterable<MerchantUser> findByUserIdAndBranchId(Long userId, Long branchId);

	Iterable<MerchantUser> findByUserIdAndBranchIdAndIsActiveTrue(Long userId, Long branchId);

	Iterable<MerchantUser> findByUserIdAndIsPrimaryMerchantTrue(Long userId);

	Iterable<MerchantUser> findByRoleId(Long roleId);

	Iterable<MerchantUser> findByMerchantIdAndRoleId(Long merchantId, Long roleId);

	Iterable<MerchantUser> findByMerchantIdAndPermissionsScope(Long merchantId, String permissionsScope);

	boolean existsByUserIdAndMerchantId(Long userId, Long merchantId);

	boolean existsByUserIdAndBranchId(Long userId, Long branchId);

	boolean existsByUserIdAndMerchantIdAndBranchId(Long userId, Long merchantId, Long branchId);

	void deleteByUserIdAndMerchantId(Long userId, Long merchantId);

	void deleteByBranchId(Long branchId);
}
