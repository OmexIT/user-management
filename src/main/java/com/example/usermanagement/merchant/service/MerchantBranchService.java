package com.example.usermanagement.merchant.service;

import com.example.usermanagement.shared.exception.EntityExistsException;
import com.example.usermanagement.shared.exception.EntityNotFoundException;
import com.example.usermanagement.merchant.model.MerchantBranch;
import com.example.usermanagement.user.model.Permission;
import com.example.usermanagement.merchant.repository.MerchantBranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantBranchService {

	private final MerchantBranchRepository merchantBranchRepository;
	private final JdbcTemplate jdbcTemplate;

	@Transactional
	public MerchantBranch createBranch(Long merchantId, String branchCode, String branchName, String branchType) {
		if (merchantBranchRepository.existsByMerchantIdAndBranchCode(merchantId, branchCode)) {
			throw new EntityExistsException("Branch", "code", branchCode);
		}

		MerchantBranch branch = new MerchantBranch(merchantId, branchCode, branchName, branchType);
		return merchantBranchRepository.save(branch);
	}

	@Transactional
	public MerchantBranch createSubBranch(Long merchantId, Long parentBranchId, String branchCode, String branchName,
			String branchType) {
		if (merchantBranchRepository.existsByMerchantIdAndBranchCode(merchantId, branchCode)) {
			throw new EntityExistsException("Branch", "code", branchCode);
		}

		MerchantBranch branch = new MerchantBranch(merchantId, parentBranchId, branchCode, branchName, branchType);
		return merchantBranchRepository.save(branch);
	}

	public MerchantBranch getBranchById(Long id) {
		return merchantBranchRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("MerchantBranch", id));
	}

	public List<MerchantBranch> getMerchantBranches(Long merchantId) {
		String sql = """
				SELECT * FROM merchant_branches
				WHERE merchant_id = ? AND is_active = true
				ORDER BY branch_name
				""";

		return jdbcTemplate.query(sql, new BranchRowMapper(), merchantId);
	}

	public List<MerchantBranch> getBranchHierarchy(Long branchId) {
		String sql = """
				WITH RECURSIVE branch_hierarchy AS (
				    SELECT id, merchant_id, parent_branch_id, branch_code, branch_name, branch_type, 0 as level
				    FROM merchant_branches WHERE id = ?
				    UNION ALL
				    SELECT b.id, b.merchant_id, b.parent_branch_id, b.branch_code, b.branch_name, b.branch_type, h.level - 1
				    FROM merchant_branches b
				    INNER JOIN branch_hierarchy h ON b.id = h.parent_branch_id
				)
				SELECT * FROM branch_hierarchy ORDER BY level
				""";

		return jdbcTemplate.query(sql, new BranchRowMapper(), branchId);
	}

	public List<Long> getBranchAndDescendantIds(Long branchId) {
		String sql = """
				WITH RECURSIVE branch_descendants AS (
				    SELECT id FROM merchant_branches WHERE id = ?
				    UNION ALL
				    SELECT b.id
				    FROM merchant_branches b
				    INNER JOIN branch_descendants d ON b.parent_branch_id = d.id
				)
				SELECT id FROM branch_descendants
				""";

		return jdbcTemplate.queryForList(sql, Long.class, branchId);
	}

	public List<Permission> getUserBranchPermissions(Long userId, Long merchantId, Long branchId) {
		String sql = """
				WITH RECURSIVE branch_hierarchy AS (
				    SELECT id FROM merchant_branches WHERE id = ?
				    UNION ALL
				    SELECT b.parent_branch_id
				    FROM merchant_branches b
				    INNER JOIN branch_hierarchy h ON b.id = h.id
				    WHERE b.parent_branch_id IS NOT NULL
				)
				SELECT DISTINCT p.id, p.permission_code, p.resource, p.action, p.description
				FROM permissions p
				JOIN role_permissions rp ON p.id = rp.permission_id
				JOIN merchant_users mu ON mu.role_id = rp.role_id
				WHERE mu.user_id = ?
				  AND mu.merchant_id = ?
				  AND mu.is_active = true
				  AND (
				      mu.permissions_scope = 'MERCHANT_WIDE'
				      OR (mu.permissions_scope = 'BRANCH_ONLY' AND mu.branch_id = ?)
				      OR (mu.permissions_scope = 'BRANCH_AND_BELOW' AND mu.branch_id IN (SELECT id FROM branch_hierarchy))
				  )
				""";

		return jdbcTemplate.query(sql, new PermissionRowMapper(), branchId, userId, merchantId, branchId);
	}

	public boolean hasPermissionAtBranch(Long userId, Long merchantId, Long branchId, String resource, String action) {
		List<Permission> permissions = getUserBranchPermissions(userId, merchantId, branchId);

		return permissions.stream().anyMatch(p -> p.getResource().equals(resource) && p.getAction().equals(action));
	}

	@Transactional
	public void deactivateBranch(Long id) {
		jdbcTemplate.update("UPDATE merchant_branches SET is_active = false WHERE id = ?", id);
	}

	@Transactional
	public void activateBranch(Long id) {
		jdbcTemplate.update("UPDATE merchant_branches SET is_active = true WHERE id = ?", id);
	}

	private static class BranchRowMapper implements RowMapper<MerchantBranch> {
		@Override
		public MerchantBranch mapRow(ResultSet rs, int rowNum) throws SQLException {
			MerchantBranch branch = new MerchantBranch();
			branch.setId(rs.getLong("id"));
			branch.setMerchantId(rs.getLong("merchant_id"));
			branch.setParentBranchId(rs.getObject("parent_branch_id", Long.class));
			branch.setBranchCode(rs.getString("branch_code"));
			branch.setBranchName(rs.getString("branch_name"));
			branch.setBranchType(rs.getString("branch_type"));
			branch.setIsActive(rs.getBoolean("is_active"));
			return branch;
		}
	}

	private static class PermissionRowMapper implements RowMapper<Permission> {
		@Override
		public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
			Permission permission = new Permission();
			permission.setId(rs.getLong("id"));
			permission.setPermissionCode(rs.getString("permission_code"));
			permission.setResource(rs.getString("resource"));
			permission.setAction(rs.getString("action"));
			permission.setDescription(rs.getString("description"));
			return permission;
		}
	}
}
