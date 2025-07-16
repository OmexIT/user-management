package com.example.usermanagement.user.repository;

import com.example.usermanagement.user.model.UserPreference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends CrudRepository<UserPreference, Long> {

	Iterable<UserPreference> findByUserId(Long userId);

	Optional<UserPreference> findByUserIdAndPreferenceKey(Long userId, String preferenceKey);

	Iterable<UserPreference> findByUserIdAndCategory(Long userId, String category);

	Iterable<UserPreference> findByUserIdAndIsSensitiveFalse(Long userId);

	void deleteByUserIdAndPreferenceKey(Long userId, String preferenceKey);

	void deleteByUserIdAndCategory(Long userId, String category);

	boolean existsByUserIdAndPreferenceKey(Long userId, String preferenceKey);
}
