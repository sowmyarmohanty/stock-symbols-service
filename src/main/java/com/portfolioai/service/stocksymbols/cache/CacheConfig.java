package com.portfolioai.service.stocksymbols.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;

@Configuration
public class CacheConfig {
    public CacheManager cacheManager(){
        ConcurrentMapCacheManager mgr = new ConcurrentMapCacheManager();
        mgr.setCacheNames(asList("symbols"));
        return mgr;
    }
}
