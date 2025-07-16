package com.example.usermanagement.merchant.repository;

import com.example.usermanagement.merchant.model.MerchantBranch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantBranchRepository extends CrudRepository<MerchantBranch, Long> {

	Iterable<MerchantBranch> findByMerchantId(Long merchantId);

	Iterable<MerchantBranch> findByMerchantIdAndIsActiveTrue(Long merchantId);

	Iterable<MerchantBranch> findByParentBranchId(Long parentBranchId);

	Iterable<MerchantBranch> findByParentBranchIdAndIsActiveTrue(Long parentBranchId);

	Iterable<MerchantBranch> findByMerchantIdAndParentBranchIdIsNull(Long merchantId);

	Iterable<MerchantBranch> findByMerchantIdAndParentBranchIdIsNullAndIsActiveTrue(Long merchantId);

	Iterable<MerchantBranch> findByBranchCode(String branchCode);

	Iterable<MerchantBranch> findByMerchantIdAndBranchType(Long merchantId, String branchType);

	boolean existsByMerchantIdAndBranchCode(Long merchantId, String branchCode);

	boolean existsByMerchantIdAndBranchCodeAndIdNot(Long merchantId, String branchCode, Long id);
}
