package com.example.mongodb.configs;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteCacheConfiguration {

    @Bean
    public Ignite igniteInstance() {
        IgniteConfiguration cfg = new IgniteConfiguration();

        cfg.setClientMode(true);

        return Ignition.start(cfg);
    }

//    @Bean
//    public CacheManager cacheManager() {
//        SpringCacheManager cacheManager = new SpringCacheManager();
//        cacheManager.setConfiguration(igniteClientConfiguration());
//        return cacheManager;
//    }
}
