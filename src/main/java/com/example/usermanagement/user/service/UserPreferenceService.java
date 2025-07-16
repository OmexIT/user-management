package com.example.usermanagement.user.service;

import com.example.usermanagement.user.model.UserPreference;
import com.example.usermanagement.user.repository.UserPreferenceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPreferenceService {

	private final UserPreferenceRepository userPreferenceRepository;
	private final ObjectMapper objectMapper;

	@Transactional
	public UserPreference setPreference(Long userId, String key, String value, String type, String category) {
		Optional<UserPreference> existing = userPreferenceRepository.findByUserIdAndPreferenceKey(userId, key);

		UserPreference preference;
		if (existing.isPresent()) {
			preference = existing.get();
			preference.setPreferenceValue(value);
			preference.setPreferenceType(type);
			preference.setCategory(category);
		} else {
			preference = new UserPreference(userId, key, value, type, category);
		}

		return userPreferenceRepository.save(preference);
	}

	public Optional<UserPreference> getPreference(Long userId, String key) {
		return userPreferenceRepository.findByUserIdAndPreferenceKey(userId, key);
	}

	public String getPreferenceValue(Long userId, String key, String defaultValue) {
		return getPreference(userId, key).map(UserPreference::getPreferenceValue).orElse(defaultValue);
	}

	public Iterable<UserPreference> getUserPreferences(Long userId) {
		return userPreferenceRepository.findByUserId(userId);
	}

	public Iterable<UserPreference> getUserPreferencesByCategory(Long userId, String category) {
		return userPreferenceRepository.findByUserIdAndCategory(userId, category);
	}

	public Map<String, String> getUserPreferencesAsMap(Long userId) {
		Map<String, String> preferencesMap = new HashMap<>();
		Iterable<UserPreference> preferences = getUserPreferences(userId);

		preferences.forEach(pref -> {
			if (!pref.getIsSensitive()) {
				preferencesMap.put(pref.getPreferenceKey(), pref.getPreferenceValue());
			}
		});

		return preferencesMap;
	}

	@Transactional
	public void deletePreference(Long userId, String key) {
		userPreferenceRepository.deleteByUserIdAndPreferenceKey(userId, key);
	}

	@Transactional
	public void deletePreferencesByCategory(Long userId, String category) {
		userPreferenceRepository.deleteByUserIdAndCategory(userId, category);
	}

	@Transactional
	public UserPreference setSensitivePreference(Long userId, String key, String value, String category) {
		UserPreference preference = setPreference(userId, key, value, "ENCRYPTED", category);
		preference.setIsSensitive(true);
		return userPreferenceRepository.save(preference);
	}

	@Transactional
	public void setJsonPreference(Long userId, String key, Object value, String category) {
		try {
			String jsonValue = objectMapper.writeValueAsString(value);
			setPreference(userId, key, jsonValue, "JSON", category);
		} catch (JsonProcessingException e) {
			log.error("Failed to serialize preference value to JSON", e);
			throw new RuntimeException("Failed to save JSON preference", e);
		}
	}

	public <T> Optional<T> getJsonPreference(Long userId, String key, Class<T> valueType) {
		return getPreference(userId, key).filter(pref -> "JSON".equals(pref.getPreferenceType())).map(pref -> {
			try {
				return objectMapper.readValue(pref.getPreferenceValue(), valueType);
			} catch (JsonProcessingException e) {
				log.error("Failed to deserialize JSON preference", e);
				return null;
			}
		});
	}

	@Transactional
	public void setBooleanPreference(Long userId, String key, boolean value, String category) {
		setPreference(userId, key, String.valueOf(value), "BOOLEAN", category);
	}

	public boolean getBooleanPreference(Long userId, String key, boolean defaultValue) {
		String value = getPreferenceValue(userId, key, String.valueOf(defaultValue));
		return Boolean.parseBoolean(value);
	}

	@Transactional
	public void setIntegerPreference(Long userId, String key, int value, String category) {
		setPreference(userId, key, String.valueOf(value), "INTEGER", category);
	}

	public int getIntegerPreference(Long userId, String key, int defaultValue) {
		String value = getPreferenceValue(userId, key, String.valueOf(defaultValue));
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@Transactional
	public void copyPreferences(Long sourceUserId, Long targetUserId) {
		Iterable<UserPreference> sourcePreferences = getUserPreferences(sourceUserId);

		sourcePreferences.forEach(pref -> {
			if (!pref.getIsSensitive()) {
				setPreference(targetUserId, pref.getPreferenceKey(), pref.getPreferenceValue(),
						pref.getPreferenceType(), pref.getCategory());
			}
		});
	}
}
