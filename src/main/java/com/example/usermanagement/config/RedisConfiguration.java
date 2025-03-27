package com.example.usermanagement.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisConfiguration.CachingProperties.class)
public class RedisConfiguration {

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		return RedisCacheManager.builder(connectionFactory).cacheDefaults(cacheConfiguration()).build();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplateObject(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

		template.setDefaultSerializer(serializer);
		template.setKeySerializer(serializer);
		template.setValueSerializer(serializer);

		return template;
	}

	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig().prefixCacheNameWith("dfs:").entryTtl(Duration.ofMinutes(60))
				.disableCachingNullValues().serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}

	@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(final CachingProperties config,
			RedisCacheConfiguration cacheConfiguration) {
		return builder -> {
			Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
			config.getCacheConfig()
					.forEach((cacheName, cacheProps) -> cacheProps.getTimeToLive().ifPresent(customTtl -> {
						log.info("Configuring redis cache {} with custom ttl [{}]", cacheName,
								cacheProps.getTimeToLive().get());
						configurationMap.put(cacheName, cacheConfiguration.entryTtl(Duration.ofSeconds(customTtl)));
					}));
			builder.withInitialCacheConfigurations(configurationMap);
		};
	}

	@Data
	@Validated
	@ConfigurationProperties(prefix = "asset-finance.caching")
	public static class CachingProperties {

		@NotNull private Integer defaultTimeToLive = 60;

		@NotNull private Map<String, CacheConfig> cacheConfig = new HashMap<>();

		@Data
		public static class CacheConfig {

			private Integer timeToLive;

			public Optional<Integer> getTimeToLive() {
				return Optional.of(this.timeToLive);
			}
		}
	}
}
