package me.laurelmay.game24;

import me.laurelmay.game24.service.util.SortedListKeyGenerator;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableCaching
@ConfigurationPropertiesScan
public class Solve24Configuration {

  @Bean
  CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    cacheManager.setCaches(List.of(new ConcurrentMapCache("solutions")));
    return cacheManager;
  }

  @Bean
  SortedListKeyGenerator sortedListKeyGenerator() {
    return new SortedListKeyGenerator();
  }
}
