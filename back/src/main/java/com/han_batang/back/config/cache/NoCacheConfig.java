package com.han_batang.back.config.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("nocache")
public class NoCacheConfig {
    //gradlew bootRun --args="--spring.profiles.active=nocache"
    
    @Bean
    public CacheManager cacheManager() {
        return new NoOpCacheManager();
    }
}
